package com.kamrul.server.controllers;

import com.kamrul.server.dto.PostDTO;
import com.kamrul.server.dto.MedalDTO;
import com.kamrul.server.exception.ResourceNotFoundException;
import com.kamrul.server.exception.UnauthorizedException;
import com.kamrul.server.models.compositeKey.UserAndPostCompositeKey;
import com.kamrul.server.models.medal.Medal;
import com.kamrul.server.models.medal.MedalType;
import com.kamrul.server.models.post.Post;
import com.kamrul.server.models.user.User;
import com.kamrul.server.repositories.GeneralQueryRepository;
import com.kamrul.server.repositories.PostRepository;
import com.kamrul.server.repositories.MedalRepository;
import com.kamrul.server.repositories.UserRepository;
import com.kamrul.server.security.jwt.JWTUtil;
import com.kamrul.server.utils.Converters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.Optional;

import static com.kamrul.server.utils.GeneralResponseMSG.*;

@CrossOrigin
@RestController
@RequestMapping("/api/medal")
public class MedalController {

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MedalRepository medalRepository;

    @PutMapping
    @Transactional(rollbackOn = {Exception.class})
    ResponseEntity<?> giveMedalOnThePostByUser(
            @RequestBody MedalDTO medalDataFromUser,
            @RequestHeader("Authorization") String jwt)
            throws UnauthorizedException, ResourceNotFoundException {

        Long userId= JWTUtil.getUserIdFromJwt(jwt);
        Long postId= medalDataFromUser.getPostId();
        MedalType userProvidedMedalType=medalDataFromUser.getMedalType();

        Post post=GeneralQueryRepository.getByID(
                postRepository,
                medalDataFromUser.getPostId(),
                POST_NOT_FOUND_MSG
        );

        User user=GeneralQueryRepository.getByID(
                userRepository,
                userId,
                USER_NOT_FOUND_MSG
        );

        UserAndPostCompositeKey userAndPostCompositeKey = new UserAndPostCompositeKey(userId,postId);

        Optional<MedalDTO> optionalMedal= medalRepository.findMedalByCompositeKey(userAndPostCompositeKey);

        /* Required SQL Query Optimization */
        if(!optionalMedal.isPresent())
        {
            Medal medal =new Medal(userAndPostCompositeKey,user, post, userProvidedMedalType);
            post.addMedalCount(userProvidedMedalType);
            medalRepository.save(medal);
        }
        else
        {
            /* Need to Perform Indexing on (userId,postId) */
            MedalDTO previousMedalDto=optionalMedal.get();
            Medal previousMedal= new Medal(userAndPostCompositeKey,user,post,previousMedalDto.getMedalType());
            Medal updatedMedal=new Medal(userAndPostCompositeKey,user,post,userProvidedMedalType);
            post.removePreviousMedalCount(previousMedalDto.getMedalType());
            post.addMedalCount(userProvidedMedalType);
            if(userProvidedMedalType==MedalType.NO_MEDAL)
                medalRepository.deleteInBatch(Arrays.asList(previousMedal));
            else
                medalRepository.save(updatedMedal);

        }
        PostDTO postDTO=new PostDTO();
        postDTO= Converters.convert(post,postDTO);
        postDTO.setMedalTypeProvidedByLoggedInUser(userProvidedMedalType);
        return new ResponseEntity<>(postDTO, HttpStatus.OK);
    }

}
