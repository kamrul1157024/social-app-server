package com.kamrul.blog.controllers;


import com.kamrul.blog.dto.CommentDTO;
import com.kamrul.blog.exception.ResourceNotFoundException;
import com.kamrul.blog.exception.UnauthorizedException;
import com.kamrul.blog.models.comment.Comment;
import com.kamrul.blog.models.post.Post;
import com.kamrul.blog.models.user.User;
import com.kamrul.blog.repositories.CommentRepository;
import com.kamrul.blog.repositories.GeneralQueryRepository;
import com.kamrul.blog.repositories.PostRepository;
import com.kamrul.blog.repositories.UserRepository;
import com.kamrul.blog.utils.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.List;

import static com.kamrul.blog.utils.GeneralResponseMSG.*;


@CrossOrigin
@RestController
@RequestMapping("/api/comment")
public class CommentController {
    @Autowired
    CommentRepository commentRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    PostRepository postRepository;

    @GetMapping
    public ResponseEntity<?> getCommentById(@RequestParam(value = "id") Long commentId)
            throws ResourceNotFoundException {
        Comment comment= GeneralQueryRepository.getByID(
                commentRepository,
                commentId,
                COMMENT_NOT_FOUND_MSG
        );
        return new ResponseEntity<>(comment, HttpStatus.OK);
    }

    @GetMapping("/post/{postId}")
    public ResponseEntity<?> getCommentByPostId(@PathVariable(value = "postId") Long postId) {
        List<Comment> comments=commentRepository.getCommentsByPostID(postId);
        return new ResponseEntity<>(comments,HttpStatus.OK);
    }

    @PostMapping
    @Transactional(rollbackOn = {Exception.class})
    public ResponseEntity<?> createComment(@RequestBody CommentDTO commentDTO)
            throws ResourceNotFoundException, UnauthorizedException {

        User user= GeneralQueryRepository.getByID(
                userRepository,
                GeneralQueryRepository.getCurrentlyLoggedInUserId(),
                USER_NOT_FOUND_MSG
        );

        Post post= GeneralQueryRepository.getByID(
                postRepository,
                commentDTO.getPostId(),
                POST_NOT_FOUND_MSG
        );

        Comment comment=new Comment();

        comment.setCommentText(commentDTO.getCommentText());
        comment.setUser(user);
        comment.setPost(post);

        commentRepository.save(comment);

        return new ResponseEntity<>(comment, HttpStatus.ACCEPTED);
    }

    @PutMapping
    @Transactional(rollbackOn = {Exception.class})
    public ResponseEntity<?> updateComment(@RequestBody CommentDTO commentDTO)
            throws ResourceNotFoundException, UnauthorizedException {

        User user= GeneralQueryRepository.getByID(
                userRepository,
                GeneralQueryRepository.getCurrentlyLoggedInUserId(),
                USER_NOT_FOUND_MSG
        );

        Comment comment= GeneralQueryRepository.getByID(
                commentRepository,
                commentDTO.getCommentId(),
                COMMENT_NOT_FOUND_MSG
        );


        if(!user.equals(comment.getUser()))
            throw new UnauthorizedException("User Do no have permission to update this post");

        comment.setCommentText(commentDTO.getCommentText());
        commentRepository.save(comment);

        return new ResponseEntity<>(comment,HttpStatus.OK);
    }


    @DeleteMapping
    public ResponseEntity<?> deleteComment(@RequestParam(value = "id") Long commentId)
            throws ResourceNotFoundException, UnauthorizedException {

        User user= GeneralQueryRepository.getByID(
                userRepository,
                GeneralQueryRepository.getCurrentlyLoggedInUserId(),
                USER_NOT_FOUND_MSG
        );

        Comment comment= GeneralQueryRepository.getByID(
                commentRepository,
                commentId,
                COMMENT_NOT_FOUND_MSG
        );


        if(!user.equals(comment.getUser()))
            throw new UnauthorizedException("User Do no have permission to delete this Comment");

        commentRepository.deleteInBatch(Arrays.asList(comment));

        return new ResponseEntity<>(new Message("Comment Deleted Successfully"),HttpStatus.OK);
    }

}
