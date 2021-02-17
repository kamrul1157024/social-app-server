package com.kamrul.blog.controllers;

import com.kamrul.blog.dto.PostDTO;
import com.kamrul.blog.exception.ResourceNotFoundException;
import com.kamrul.blog.exception.UnauthorizedException;
import com.kamrul.blog.models.Post;
import com.kamrul.blog.models.User;
import com.kamrul.blog.repositories.GeneralQuery;
import com.kamrul.blog.repositories.PostRepository;
import com.kamrul.blog.repositories.UserRepository;
import com.kamrul.blog.utils.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

import static com.kamrul.blog.utils.RESPONSE_MSG.*;

@RestController
@RequestMapping("/api/post")
public class PostController {
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserRepository userRepository;


    @GetMapping
    public ResponseEntity<?> getPostById(@RequestParam(value = "id") Long postId)
            throws ResourceNotFoundException {
        Post post= GeneralQuery.getByID(
                postRepository,
                postId,
                POST_NOT_FOUND_MSG
                );
        return new ResponseEntity<>(post,HttpStatus.OK);
    }

    @GetMapping("/page/{pageId}")
    public ResponseEntity<?> getPostByPage(@PathVariable(value = "pageId") Long pageId)
            throws ResourceNotFoundException {
        List<Post> postList=postRepository.getTopPost();
        return new ResponseEntity<>(postList,HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> createPost(@RequestBody PostDTO postDTO)
            throws ResourceNotFoundException, UnauthorizedException {
        User user= GeneralQuery.getByID(
                userRepository,
                GeneralQuery.getCurrentlyLoggedInUserId(),
                USER_NOT_FOUND_MSG
        );

        Post post=new Post();
        post.setPostText(postDTO.getPostText());
        post.setUser(user);
        postRepository.save(post);

        return new ResponseEntity<>(post, HttpStatus.ACCEPTED);
    }

    @PutMapping
    public ResponseEntity<?> updatePost(@RequestBody PostDTO postDetails) throws ResourceNotFoundException, UnauthorizedException {

        User user= GeneralQuery.getByID(
                userRepository,
                GeneralQuery.getCurrentlyLoggedInUserId(),
                USER_NOT_FOUND_MSG
        );

        Post post= GeneralQuery.getByID(
                postRepository,
                postDetails.getPostId(),
                POST_NOT_FOUND_MSG
        );

        if(!user.equals(post.getUser()))
            throw new UnauthorizedException("User Do no have permission to Update this post");

        post.setPostText(postDetails.getPostText());
        postRepository.save(post);

        return new ResponseEntity<>(post,HttpStatus.OK);
    }

    @PutMapping("/upvote")
    public ResponseEntity<?> upVotePost(@RequestParam("postId") Long postId ,@RequestParam("userId") Long userId ) throws ResourceNotFoundException {
        Post post= GeneralQuery.getByID(postRepository,postId,"Post does not Exist");
        User user= GeneralQuery.getByID(userRepository,userId,"User does not Exist");

        //do nothing

        postRepository.save(post);
        return new ResponseEntity<>(post,HttpStatus.OK);
    }

    @PutMapping("/downvote")
    public ResponseEntity<?> downVotePost(@RequestParam("postId") Long postId ,@RequestParam("userId") Long userId) throws ResourceNotFoundException {
        Post post= GeneralQuery.getByID(postRepository,postId,"Post does not Exist");
        User user= GeneralQuery.getByID(userRepository,userId,"User does not Exist");

        //do nothing

        postRepository.save(post);
        return new ResponseEntity<>(post,HttpStatus.OK);
    }


    @DeleteMapping
    public ResponseEntity<?> deletePost(@RequestParam(value = "id") Long postId)
            throws ResourceNotFoundException, UnauthorizedException {
        User user= GeneralQuery.getByID(
                userRepository,
                GeneralQuery.getCurrentlyLoggedInUserId(),
                USER_NOT_FOUND_MSG
        );
        Post post= GeneralQuery.getByID(
                postRepository,
                postId,
                POST_NOT_FOUND_MSG
        );

        if(!user.equals(post.getUser()))
            throw new UnauthorizedException("User Do no have permission to delete this post");

        postRepository.deleteInBatch(Arrays.asList(post));

        return new ResponseEntity<>(new Message("Post Deleted Successfully"),HttpStatus.OK);
    }


}
