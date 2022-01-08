package com.kamrul.server.controllers;

import com.kamrul.server.dto.PostDTO;
import com.kamrul.server.exception.ResourceNotFoundException;
import com.kamrul.server.models.compositeKey.UserAndPostCompositeKey;
import com.kamrul.server.models.medal.Medal;
import com.kamrul.server.models.medal.MedalType;
import com.kamrul.server.models.post.Post;
import com.kamrul.server.models.user.User;
import com.kamrul.server.repositories.GeneralQueryRepository;
import com.kamrul.server.repositories.PostRepository;
import com.kamrul.server.repositories.MedalRepository;
import com.kamrul.server.repositories.UserRepository;
import com.kamrul.server.utils.Converters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.Optional;

import static com.kamrul.server.configuration.GeneralResponseMSG.*;

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

    @PutMapping("/post/{postId}/medal/{medalType}")
    @Transactional(rollbackOn = {Exception.class})
    ResponseEntity<?> giveMedalOnThePost(
            @PathVariable("medalType")MedalType userProvidedMedalType,
            @PathVariable("postId")Long postId, @RequestAttribute("userId") Long userId
    ) throws ResourceNotFoundException {
        Post post=GeneralQueryRepository.getByID(postRepository, postId, POST_NOT_FOUND);
        User user=GeneralQueryRepository.getByID(userRepository, userId, USER_NOT_FOUND);

        UserAndPostCompositeKey userAndPostCompositeKey = new UserAndPostCompositeKey(userId,postId);
        Optional<Medal> optionalMedal= medalRepository.findMedalByUserAndPostCompositeKey(userAndPostCompositeKey);

        if(!optionalMedal.isPresent()) {
            Medal medal =new Medal(userAndPostCompositeKey,user, post, userProvidedMedalType);
            post.addMedalCount(userProvidedMedalType);
            medalRepository.save(medal);
        }
        else {
            Medal previousMedalDto=optionalMedal.get();
            Medal previousMedal= new Medal(userAndPostCompositeKey,user,post,previousMedalDto.getMedalType());
            Medal updatedMedal=new Medal(userAndPostCompositeKey,user,post,userProvidedMedalType);
            post.removePreviousMedalCount(previousMedalDto.getMedalType());
            post.addMedalCount(userProvidedMedalType);
            if(userProvidedMedalType==MedalType.NO_MEDAL)
                medalRepository.deleteInBatch(Arrays.asList(previousMedal));
            else
                medalRepository.save(updatedMedal);
        }
        PostDTO postDTO= Converters.convert(post);
        postDTO.setMedalTypeProvidedByLoggedInUser(userProvidedMedalType);
        return new ResponseEntity<>(postDTO, HttpStatus.OK);
    }
}
