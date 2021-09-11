package com.kamrul.server.controllers;

import com.kamrul.server.dto.FollowDTO;
import com.kamrul.server.dto.UserDTO;
import com.kamrul.server.exception.ResourceNotFoundException;
import com.kamrul.server.exception.UnauthorizedException;
import com.kamrul.server.models.user.User;
import com.kamrul.server.repositories.FollowerRepository;
import com.kamrul.server.repositories.GeneralQueryRepository;
import com.kamrul.server.repositories.UserRepository;
import com.kamrul.server.utils.Converters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.Set;
import java.util.stream.Collectors;
import static com.kamrul.server.utils.GeneralResponseMSG.USER_NOT_FOUND;

@CrossOrigin
@RestController
@RequestMapping("/api/follow")
public class FollowerController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private FollowerRepository followerRepository;

    @GetMapping("/allFollowedUserId")
    ResponseEntity<?> getAllUserIdLoggedInUserFollows(@RequestAttribute("userId") Long loggedInUserId) {
        /*Need to perform SQL Query Optimization*/
        Set<User> userFollows = followerRepository.getAllUserCurrentUserFollows(loggedInUserId);
        Set<Long> userIds=userFollows
                .stream()
                .sorted()
                .map(user -> user.getUserId())
                .collect(Collectors.toSet());
        return new ResponseEntity<>(userIds,HttpStatus.OK);
    }

    /*
    *It it better to perform operation on followee list as user can be restrict how many people
    * they can follow, But a followee May have lots of follower,so traversing will be slow on this side
    */

    @PutMapping
    @Transactional(rollbackOn = {Exception.class})
    ResponseEntity<?>  followUserById(@RequestBody FollowDTO followDTO, @RequestAttribute("userId") Long loggedInUserId)
            throws UnauthorizedException, ResourceNotFoundException {
        Long loggedInUsedWantsToFollowUserId= followDTO.getFollow();

        User loggedInUser= GeneralQueryRepository.getByID(userRepository, loggedInUserId, USER_NOT_FOUND);
        User loggedInUserWantsToFollowUser = GeneralQueryRepository.getByID(userRepository, loggedInUsedWantsToFollowUserId, USER_NOT_FOUND);

        /* May required optimization on Saving on Table */
        Set<User> loggedInUserCurrentlyFollowed= loggedInUser.getFollowed();
        Boolean isPreviouslyFollowedByUser= loggedInUserCurrentlyFollowed
                .stream()
                .anyMatch(user -> user.getUserId()==loggedInUsedWantsToFollowUserId);
        if(isPreviouslyFollowedByUser) {
            /* Will get deleted Automatically From follow table */
            loggedInUserCurrentlyFollowed.remove(loggedInUserWantsToFollowUser);
            loggedInUser.setFollowed(loggedInUserCurrentlyFollowed);
            loggedInUserWantsToFollowUser
                    .setTotalNumberOfFollower(loggedInUserWantsToFollowUser.getTotalNumberOfFollower()-1);
            loggedInUser
                    .setTotalNumberOfUserFollowed(loggedInUser.getTotalNumberOfUserFollowed()-1);
        }
        else {
            loggedInUserCurrentlyFollowed.add(loggedInUserWantsToFollowUser);
            loggedInUser.setFollowed(loggedInUserCurrentlyFollowed);
            loggedInUserWantsToFollowUser
                    .setTotalNumberOfFollower(loggedInUserWantsToFollowUser.getTotalNumberOfFollower()+1);
            loggedInUser
                    .setTotalNumberOfUserFollowed(loggedInUser.getTotalNumberOfUserFollowed()+1);
        }

        userRepository.save(loggedInUser);
        userRepository.save(loggedInUserWantsToFollowUser);

        UserDTO loggedInUserDto= Converters.convert(loggedInUser);
        loggedInUserDto.setFollowedByCurrentlyLoggedInUser(isPreviouslyFollowedByUser^true);
        return new ResponseEntity<>(loggedInUserDto,HttpStatus.OK);
    }
}
