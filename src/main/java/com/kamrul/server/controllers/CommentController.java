package com.kamrul.server.controllers;


import com.kamrul.server.dto.CommentDTO;
import com.kamrul.server.exception.ResourceNotFoundException;
import com.kamrul.server.exception.UnauthorizedException;
import com.kamrul.server.models.comment.Comment;
import com.kamrul.server.models.post.Post;
import com.kamrul.server.models.user.User;
import com.kamrul.server.repositories.CommentRepository;
import com.kamrul.server.repositories.GeneralQueryRepository;
import com.kamrul.server.repositories.PostRepository;
import com.kamrul.server.repositories.UserRepository;
import com.kamrul.server.services.verify.Verifier;
import com.kamrul.server.services.verify.exception.VerificationException;
import com.kamrul.server.utils.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.List;

import static com.kamrul.server.utils.GeneralResponseMSG.*;


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
    @Autowired
    Verifier<Comment> commentVerifier;


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
            throws ResourceNotFoundException, UnauthorizedException, VerificationException {

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

        commentVerifier.verify(comment);

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
