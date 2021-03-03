package com.kamrul.blog.controllers;

import com.kamrul.blog.dto.PostDTO;
import com.kamrul.blog.dto.MedalDTO;
import com.kamrul.blog.exception.ResourceNotFoundException;
import com.kamrul.blog.exception.UnauthorizedException;
import com.kamrul.blog.models.MedalType;
import com.kamrul.blog.models.Post;
import com.kamrul.blog.models.Medal;
import com.kamrul.blog.models.User;
import com.kamrul.blog.repositories.GeneralQueryRepository;
import com.kamrul.blog.repositories.PostRepository;
import com.kamrul.blog.repositories.MedalRepository;
import com.kamrul.blog.repositories.UserRepository;
import com.kamrul.blog.utils.Converters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Optional;

import static com.kamrul.blog.utils.GeneralResponseMSG.*;

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
    ResponseEntity<?> giveMedalOnThePostByUser(@RequestBody MedalDTO medalDataFromUser)
            throws UnauthorizedException, ResourceNotFoundException {

        Post post=GeneralQueryRepository.getByID(
                postRepository,
                medalDataFromUser.getPostId(),
                POST_NOT_FOUND_MSG
        );

        User user=GeneralQueryRepository.getByID(
                userRepository,
                GeneralQueryRepository.getCurrentlyLoggedInUserId(),
                USER_NOT_FOUND_MSG
        );

        Optional<MedalDTO> optionalBah= medalRepository.
                findMedalByUserIdAndPostID(user.getUserId(),post.getPostId());
        final MedalType userProvidedMedalType=medalDataFromUser.getMedalType();

        /* Required SQL Query Optimization */
        if(!optionalBah.isPresent())
        {
            Medal medal =new Medal(user, post, userProvidedMedalType);
            post.addMedalCount(userProvidedMedalType);
            medalRepository.save(medal);
        }
        else
        {
            /* Need to Perform Indexing on (userId,postId) */
            MedalDTO previousMedalDto=optionalBah.get();
            Medal medal=new Medal(previousMedalDto.getMedalId(),user,post);
            Medal updatedMedal=new Medal(medal,userProvidedMedalType);
            post.removePreviousMedalCount(previousMedalDto.getMedalType());
            post.addMedalCount(userProvidedMedalType);
            if(userProvidedMedalType==MedalType.NO_MEDAL)
                medalRepository.deleteInBatch(Arrays.asList(medal));
            else
                medalRepository.save(updatedMedal);
        }
        PostDTO postDTO=new PostDTO();
        postDTO= Converters.convert(post,postDTO);
        postDTO.setMedalTypeProvidedByLoggedInUser(userProvidedMedalType);
        return new ResponseEntity<>(postDTO, HttpStatus.OK);
    }

}
