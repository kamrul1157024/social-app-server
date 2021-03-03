package com.kamrul.blog.controllers;

import com.kamrul.blog.dto.MedalDTO;
import com.kamrul.blog.dto.PostDTO;
import com.kamrul.blog.dto.UserDTO;
import com.kamrul.blog.exception.ResourceNotFoundException;
import com.kamrul.blog.exception.UnauthorizedException;
import com.kamrul.blog.models.MedalType;
import com.kamrul.blog.models.Post;
import com.kamrul.blog.models.User;
import com.kamrul.blog.repositories.GeneralQueryRepository;
import com.kamrul.blog.repositories.PostRepository;
import com.kamrul.blog.repositories.UserRepository;
import com.kamrul.blog.utils.Converters;
import com.kamrul.blog.utils.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;
import static com.kamrul.blog.utils.GeneralResponseMSG.USER_NOT_FOUND_MSG;

@CrossOrigin
@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PostRepository postRepository;

    @GetMapping("/details")
    public ResponseEntity<?> getUserDetails(@RequestParam(value = "id") Long userId)
            throws ResourceNotFoundException {

        User user= GeneralQueryRepository.getByID(userRepository,userId,USER_NOT_FOUND_MSG);

        UserDTO userDTO=new UserDTO();
        userDTO=Converters.convert(user,userDTO);

        return new ResponseEntity<>(userDTO,HttpStatus.OK);
    }

    @GetMapping("/currentlyLoggedInUser")
    public ResponseEntity<?> getCurrentlyLoggedInUser()
            throws ResourceNotFoundException, UnauthorizedException {

        User user= GeneralQueryRepository.getByID(
                userRepository,
                GeneralQueryRepository.getCurrentlyLoggedInUserId(),
                USER_NOT_FOUND_MSG);

        UserDTO userDTO=new UserDTO();
        userDTO= Converters.convert(user,userDTO);

        return new ResponseEntity<>(userDTO,HttpStatus.OK);
    }

    @GetMapping("/posts")
    public ResponseEntity<?> getUserPost(
            @RequestParam(value = "id") Long userId,
            @RequestParam("pageNo") Integer pageNo
    ) {

        final Integer pageSize=10;
        Page<Post> postDTOPage=postRepository.getPostByUserId(userId, PageRequest.of(pageNo-1,pageSize));
        List<PostDTO> postDTOS = new ArrayList<>();
        postDTOPage.forEach(post -> postDTOS.add(Converters.convert(post,new PostDTO())));
        try {
            Long currentlyLoggedInUserId
                    =GeneralQueryRepository.getCurrentlyLoggedInUserId();

            List<MedalDTO> medalGivenPostOfCurrentlyLoggedInUser= postRepository
                    .getPostIdForUserIdOnWhichCurrentlyLoggedInUserGivenMedal(
                            currentlyLoggedInUserId,
                            userId
                    );

            /*<PostID,NumberOfBahOnPostGivenByUser>*/
            HashMap<Long, MedalType> medalTypeGivenByLoggedInUser=new HashMap<>();
            medalGivenPostOfCurrentlyLoggedInUser.forEach((medalDTO)->
                    medalTypeGivenByLoggedInUser
                            .put(
                                    medalDTO.getPostId(),
                                    medalDTO.getMedalType()
                            )
            );

            postDTOS.forEach(postDTO ->
                    postDTO.setMedalTypeProvidedByLoggedInUser(
                            medalTypeGivenByLoggedInUser
                                    .getOrDefault(postDTO.getPostId(),MedalType.NO_MEDAL)
                    )
            );

        } catch (UnauthorizedException unauthorizedException) {}
        return new ResponseEntity<>(postDTOS,HttpStatus.OK);
    }


    @PutMapping
    public ResponseEntity<?> updateUser(@RequestBody UserDTO userDTO)
            throws ResourceNotFoundException, UnauthorizedException {

        User user= GeneralQueryRepository.getByID(
                userRepository,
                GeneralQueryRepository.getCurrentlyLoggedInUserId(),
                USER_NOT_FOUND_MSG
        );

        user=Converters.convert(userDTO,user);
        userRepository.save(user);

        return new ResponseEntity<>(user,HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteUser()
            throws ResourceNotFoundException, UnauthorizedException {

        User user= GeneralQueryRepository.getByID(
                userRepository,
                GeneralQueryRepository.getCurrentlyLoggedInUserId(),
                USER_NOT_FOUND_MSG);

        userRepository.deleteInBatch(Arrays.asList(user));
        return new ResponseEntity<>(new Message("User deleted"),HttpStatus.OK);
    }


}
