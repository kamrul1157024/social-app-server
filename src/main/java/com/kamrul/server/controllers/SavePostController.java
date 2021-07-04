package com.kamrul.server.controllers;


import com.kamrul.server.dto.SavedPostDTO;
import com.kamrul.server.exception.ResourceNotFoundException;
import com.kamrul.server.exception.UnauthorizedException;
import com.kamrul.server.models.compositeKey.UserAndPostCompositeKey;
import com.kamrul.server.models.post.Post;
import com.kamrul.server.models.savedPost.SavedPost;
import com.kamrul.server.models.user.User;
import com.kamrul.server.repositories.GeneralQueryRepository;
import com.kamrul.server.repositories.PostRepository;
import com.kamrul.server.repositories.SavedPostRepository;
import com.kamrul.server.repositories.UserRepository;
import com.kamrul.server.security.jwt.JWTUtil;
import com.kamrul.server.utils.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.Optional;
import static com.kamrul.server.utils.GeneralResponseMSG.*;


@CrossOrigin
@RestController
@RequestMapping("/api/savePost")
public class SavePostController {

    @Autowired
    private SavedPostRepository savedPostRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/isSavedByLoggedInUser")
    ResponseEntity<?> isSavedByLoggedInUser(
            @RequestParam("postId") Long postId,
            @RequestHeader("Authorization") Optional<String> jwt)
            throws UnauthorizedException {

        SavedPostDTO savedPostDTO=new SavedPostDTO(postId,false);

        if (jwt.isEmpty()) return ResponseEntity.ok(savedPostDTO);
        Long userId=JWTUtil.getUserIdFromJwt(jwt.get());

        Optional<SavedPost> savedPost=
                savedPostRepository
                        .findById(new UserAndPostCompositeKey(userId, postId));
        savedPostDTO.setSavedByLoggedInUser(savedPost.isEmpty());

        return ResponseEntity.ok(savedPost);
    }


    @PutMapping
    @Transactional(rollbackOn = {Exception.class})
    ResponseEntity<?> saveThisPost(
            @RequestBody SavedPostDTO savedPostDTO,
            @RequestHeader("Authorization")Optional<String> jwt)
            throws UnauthorizedException, ResourceNotFoundException {

        if(jwt.isEmpty())
            throw new UnauthorizedException("Not logged In!");

        Long userId= JWTUtil.getUserIdFromJwt(jwt.get());
        Long postId= savedPostDTO.getPostId();

        User user= GeneralQueryRepository.getByID(
                userRepository,
                userId,
                USER_NOT_FOUND_MSG
        );

        Post post= GeneralQueryRepository.getByID(
                postRepository,
                postId,
                POST_NOT_FOUND_MSG
        );

        UserAndPostCompositeKey userAndPostCompositeKey=new UserAndPostCompositeKey(userId,postId);

        Optional<SavedPost> savedPost=savedPostRepository.findById(userAndPostCompositeKey);

        if(savedPost.isEmpty())
        {
            SavedPost newSavedPost= new SavedPost(userAndPostCompositeKey,user,post);
            savedPostRepository.save(newSavedPost);
            return ResponseEntity.ok(new Message("Post Saved!"));
        }
        else
        {
            savedPostRepository.deleteById(userAndPostCompositeKey);
            return ResponseEntity.ok(new Message("Post removed!"));
        }

    }




}
