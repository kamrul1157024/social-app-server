package com.kamrul.blog.controllers;

import com.kamrul.blog.dto.CommentReplyDTO;
import com.kamrul.blog.exception.ResourceNotFoundException;
import com.kamrul.blog.exception.UnauthorizedException;
import com.kamrul.blog.models.Comment;
import com.kamrul.blog.models.CommentReply;
import com.kamrul.blog.models.User;
import com.kamrul.blog.repositories.*;
import com.kamrul.blog.utils.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

import static com.kamrul.blog.utils.GeneralResponseMSG.*;

@CrossOrigin
@RestController
@RequestMapping("/api/commentReply")
public class CommentReplyController {

    @Autowired
    CommentReplyRepository commentReplyRepository;
    @Autowired
    CommentRepository commentRepository;
    @Autowired
    UserRepository userRepository;


    @GetMapping
    public ResponseEntity<?> getCommentReplyById(@RequestParam(value = "id") Long commentReplyId)
            throws ResourceNotFoundException
    {
        CommentReply commentReply= GeneralQueryRepository.getByID(
                commentReplyRepository,
                commentReplyId,
                COMMENT_NOT_FOUND_MSG
        );
        return new ResponseEntity<>(commentReply, HttpStatus.OK);
    }

    @GetMapping("/comment/{commentId}")
    public ResponseEntity<?> getCommentReplyByCommentId(@PathVariable(value = "commentId") Long commentID)
    {
        List<CommentReply> commentReplies=commentReplyRepository.getCommentRepliesByCommentId(commentID);
        return new ResponseEntity<>(commentReplies,HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> createCommentReply(@RequestBody CommentReplyDTO commentReplyDTO)
            throws ResourceNotFoundException, UnauthorizedException {

        User user= GeneralQueryRepository.getByID(
                userRepository,
                GeneralQueryRepository.getCurrentlyLoggedInUserId(),
                USER_NOT_FOUND_MSG
        );

        Comment comment= GeneralQueryRepository.getByID(
                commentRepository,
                commentReplyDTO.getCommentId(),
                COMMENT_NOT_FOUND_MSG
        );

        CommentReply commentReply=new CommentReply();

        commentReply.setCommentReplyText(commentReplyDTO.getCommentReplyText());
        commentReply.setComment(comment);
        commentReply.setUser(user);

        commentReplyRepository.save(commentReply);

        return new ResponseEntity<>(commentReply, HttpStatus.ACCEPTED);
    }

    @PutMapping
    public ResponseEntity<?> updateCommentReply(@RequestBody CommentReplyDTO commentReplyDTO)
            throws ResourceNotFoundException, UnauthorizedException
    {
        User user= GeneralQueryRepository.getByID(
                userRepository,
                GeneralQueryRepository.getCurrentlyLoggedInUserId(),
                USER_NOT_FOUND_MSG
        );

        CommentReply commentReply= GeneralQueryRepository.getByID(
                commentReplyRepository,
                commentReplyDTO.getCommentReplyId(),
                COMMENT_NOT_FOUND_MSG
        );


        if(!user.equals(commentReply.getUser()))
            throw new UnauthorizedException("User Do no have permission to Update comment");

        commentReply.setCommentReplyText(commentReplyDTO.getCommentReplyText());

        commentReplyRepository.save(commentReply);

        return new ResponseEntity<>(commentReply,HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteCommentReply(@RequestParam(value = "id") Long commentReplyId)
            throws ResourceNotFoundException, UnauthorizedException
    {
        User user= GeneralQueryRepository.getByID(
                userRepository,
                GeneralQueryRepository.getCurrentlyLoggedInUserId(),
                USER_NOT_FOUND_MSG
        );

        CommentReply commentReply= GeneralQueryRepository.getByID(
                commentReplyRepository,
                commentReplyId,
                COMMENT_NOT_FOUND_MSG
        );

        if(!user.equals(commentReply.getUser()))
            throw new UnauthorizedException("User Do no have permission to delete comment");

        commentReplyRepository.deleteInBatch(Arrays.asList(commentReply));
        return new ResponseEntity<>(new Message("Comment Deleted Successfully"),HttpStatus.OK);
    }



}
