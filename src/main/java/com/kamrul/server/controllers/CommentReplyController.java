package com.kamrul.server.controllers;

import com.kamrul.server.dto.CommentReplyDTO;
import com.kamrul.server.exception.ResourceNotFoundException;
import com.kamrul.server.exception.UnauthorizedException;
import com.kamrul.server.models.comment.Comment;
import com.kamrul.server.models.comment.CommentReply;
import com.kamrul.server.models.user.User;
import com.kamrul.server.repositories.*;
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
@RequestMapping("/api/commentReply")
public class CommentReplyController {

    @Autowired
    CommentReplyRepository commentReplyRepository;
    @Autowired
    CommentRepository commentRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    Verifier<CommentReplyDTO> commentReplyVerifier;

    @GetMapping
    public ResponseEntity<?> getCommentReplyById(@RequestParam(value = "id") Long commentReplyId)
            throws ResourceNotFoundException {
        CommentReply commentReply= GeneralQueryRepository.getByID(commentReplyRepository, commentReplyId, COMMENT_NOT_FOUND_MSG);
        return new ResponseEntity<>(commentReply, HttpStatus.OK);
    }

    @GetMapping("/comment/{commentId}")
    public ResponseEntity<?> getCommentReplyByCommentId(@PathVariable(value = "commentId") Long commentID) {
        List<CommentReply> commentReplies=commentReplyRepository.getCommentRepliesByCommentId(commentID);
        return new ResponseEntity<>(commentReplies,HttpStatus.OK);
    }

    @PostMapping
    @Transactional(rollbackOn = {Exception.class})
    public ResponseEntity<?> createCommentReply(@RequestBody CommentReplyDTO commentReplyDTO,@RequestAttribute("userId") Long userId)
            throws ResourceNotFoundException, VerificationException {
        User user= GeneralQueryRepository.getByID(userRepository,userId, USER_NOT_FOUND_MSG);
        Comment comment= GeneralQueryRepository.getByID(commentRepository, commentReplyDTO.getCommentId(), COMMENT_NOT_FOUND_MSG);

        CommentReply commentReply=new CommentReply();

        commentReply.setCommentReplyText(commentReplyDTO.getCommentReplyText());
        commentReply.setComment(comment);
        commentReply.setUser(user);

        commentReplyVerifier.verify(commentReplyDTO);
        commentReplyRepository.save(commentReply);
        return new ResponseEntity<>(commentReply, HttpStatus.ACCEPTED);
    }

    @PutMapping
    @Transactional(rollbackOn = {Exception.class})
    public ResponseEntity<?> updateCommentReply(@RequestBody CommentReplyDTO commentReplyDTO,@RequestAttribute("userId") Long userId)
            throws ResourceNotFoundException, UnauthorizedException, VerificationException {
        User user= GeneralQueryRepository.getByID(userRepository,userId, USER_NOT_FOUND_MSG);
        CommentReply commentReply= GeneralQueryRepository.getByID(commentReplyRepository, commentReplyDTO.getCommentReplyId(), COMMENT_NOT_FOUND_MSG);

        if(!user.equals(commentReply.getUser()))
            throw new UnauthorizedException("User Do no have permission to Update comment");

        commentReply.setCommentReplyText(commentReplyDTO.getCommentReplyText());

        commentReplyVerifier.verify(commentReplyDTO);
        commentReplyRepository.save(commentReply);

        return new ResponseEntity<>(commentReply,HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteCommentReply(@RequestParam(value = "id") Long commentReplyId,@RequestAttribute("userId") Long userId)
            throws ResourceNotFoundException,UnauthorizedException {
        User user= GeneralQueryRepository.getByID(userRepository,userId, USER_NOT_FOUND_MSG);
        CommentReply commentReply= GeneralQueryRepository.getByID(commentReplyRepository, commentReplyId, COMMENT_NOT_FOUND_MSG);

        if(!user.equals(commentReply.getUser()))
            throw new UnauthorizedException("User Do no have permission to delete comment");

        commentReplyRepository.deleteInBatch(Arrays.asList(commentReply));
        return new ResponseEntity<>(new Message("Comment Deleted Successfully"),HttpStatus.OK);
    }
}
