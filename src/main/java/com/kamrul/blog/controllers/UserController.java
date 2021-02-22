package com.kamrul.blog.controllers;

import com.kamrul.blog.dto.PostDTO;
import com.kamrul.blog.dto.UserDTO;
import com.kamrul.blog.exception.ResourceNotFoundException;
import com.kamrul.blog.exception.UnauthorizedException;
import com.kamrul.blog.models.Post;
import com.kamrul.blog.models.User;
import com.kamrul.blog.repositories.GeneralQueryRepository;
import com.kamrul.blog.repositories.PostRepository;
import com.kamrul.blog.repositories.UserRepository;
import com.kamrul.blog.utils.Converters;
import com.kamrul.blog.utils.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import static com.kamrul.blog.utils.GeneralResponseMessages.USER_NOT_FOUND_MSG;

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
        userDTO=Converters.convert(userDTO,user);

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
        userDTO= Converters.convert(userDTO,user);

        return new ResponseEntity<>(userDTO,HttpStatus.OK);
    }

    @GetMapping("/posts")
    public ResponseEntity<?> getUserPost(@RequestParam(value = "id") Long userId)
            throws ResourceNotFoundException {

        User user= GeneralQueryRepository.getByID(userRepository,userId,USER_NOT_FOUND_MSG);

        List<Post> posts=user.getPosts();

        List<PostDTO> postDTOS = new ArrayList<>();

        posts.forEach((post)-> {
            PostDTO postDTO=new PostDTO();
            postDTO=Converters.convert(postDTO,post);
            postDTOS.add(postDTO);
        });


        try {
            Long currentlyLoggedInUserId
                    =GeneralQueryRepository.getCurrentlyLoggedInUserId();

            List<Long> upVotedPost=postRepository
                    .getPostIdForUserIdWhichIsUpVotedByCurrentlyLoggedInUser(
                            currentlyLoggedInUserId,
                            userId
                    );


            HashMap<Long,Boolean> isUpVoted=new HashMap<>();
            upVotedPost.forEach((postId)-> isUpVoted.put(postId,true));

            Collections.sort(
                    postDTOS,
                    (postDTOa,postDTOb)-> (int) (postDTOb.getTotalUpVotes()-postDTOa.getTotalUpVotes())
            );

            postDTOS.forEach(postDTO ->
                    postDTO.setPostUpVotedByCurrentUser(
                            isUpVoted.getOrDefault(postDTO.getPostId(),false)
                    )
            );

        } catch (UnauthorizedException unauthorizedException) {}

        //Removing Comments From post
        postDTOS.forEach(postDTO -> postDTO.setComments(null));

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

        user=Converters.convert(user,userDTO);
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
