package com.kamrul.server.controllers;


import com.kamrul.server.configuration.PageConfiguration;
import com.kamrul.server.dto.CommentDTO;
import com.kamrul.server.exception.ResourceNotFoundException;
import com.kamrul.server.exception.UnauthorizedException;
import com.kamrul.server.models.comment.Comment;
import com.kamrul.server.models.comment.CommentForType;
import com.kamrul.server.models.user.User;
import com.kamrul.server.repositories.CommentRepository;
import com.kamrul.server.repositories.GeneralQueryRepository;
import com.kamrul.server.repositories.PostRepository;
import com.kamrul.server.repositories.UserRepository;
import com.kamrul.server.services.verify.Verifier;
import com.kamrul.server.utils.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.Set;

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
    Verifier<CommentDTO> commentVerifier;

    private void verifyCommentForType(CommentForType commentForType, Long commentForId)
            throws ResourceNotFoundException {
        if(commentForType==CommentForType.POST) {
            GeneralQueryRepository.getByID(postRepository,commentForId, POST_NOT_FOUND);
        }
    }

    @GetMapping("/commentForType/{commentForType}/commentFor/{commentFor}")
    public ResponseEntity<?> getCommentByPostId(
            @PathVariable(value = "commentForType") CommentForType commentForType,
            @PathVariable(value = "commentFor") Long commentFor,
            @RequestParam(value = "page") Integer page) {
        Page<Comment> comments = commentRepository.getCommentsByCommentForTypeAndAndCommentFor(
                        commentForType,
                        commentFor,
                        PageRequest.of(page, PageConfiguration.COMMENT_PAGE_SIZE)
                );
        System.out.println(comments.getContent());
        return new ResponseEntity<>(comments,HttpStatus.OK);
    }

    @PostMapping()
    @Transactional(rollbackOn = {Exception.class})
    public ResponseEntity<?> createComment(
            @RequestBody CommentDTO commentDTO,
            @RequestAttribute("userId") Long userId) throws ResourceNotFoundException{
        CommentForType commentForType = commentDTO.getCommentForType();
        Long commentFor = commentDTO.getCommentFor();
        verifyCommentForType(commentForType,commentFor);
        User user= GeneralQueryRepository.getByID(userRepository,userId, USER_NOT_FOUND);
        Comment comment=new Comment(user,commentDTO.getCommentText(), commentForType, commentFor);
        Comment commentResponse = commentRepository.save(comment);
        return new ResponseEntity<>(commentResponse, HttpStatus.ACCEPTED);
    }

    @PostMapping("/replyTo/comment/{commentId}")
    @Transactional(rollbackOn = {Exception.class})
    public ResponseEntity<?> replyToComment(
            @RequestBody CommentDTO commentDTO,
            @PathVariable("commentId")Long commentId,
            @RequestAttribute("userId")Long userId) throws ResourceNotFoundException{
        User user= GeneralQueryRepository.getByID(userRepository,userId, USER_NOT_FOUND);
        Comment comment= GeneralQueryRepository.getByID(commentRepository,commentId,COMMENT_NOT_FOUND);
        Comment reply = new Comment(user,comment,commentDTO.getCommentText());
        Comment replyResponse = commentRepository.save(reply);
        return new ResponseEntity<>(replyResponse, HttpStatus.ACCEPTED);
    }

    @PutMapping
    @Transactional(rollbackOn = {Exception.class})
    public ResponseEntity<?> updateComment(@RequestBody CommentDTO commentDTO,@RequestAttribute("userId")Long userId)
            throws ResourceNotFoundException, UnauthorizedException {
        User user= GeneralQueryRepository.getByID(userRepository, userId, USER_NOT_FOUND);
        Long commentId = commentDTO.getCommentId();
        Comment comment= GeneralQueryRepository.getByID(commentRepository,commentId,COMMENT_NOT_FOUND);
        if(!user.equals(comment.getUser()))
            throw new UnauthorizedException("User Do no have permission to update this post");
        comment.setCommentText(commentDTO.getCommentText());
        comment.setUpdated(true);
        Comment commentResponse = commentRepository.save(comment);
        return new ResponseEntity<>(commentResponse,HttpStatus.OK);
    }

    @DeleteMapping("/{commentId}")
    @Transactional(rollbackOn = {Exception.class})
    public ResponseEntity<?> deleteComment(@PathVariable(value = "commentId") Long commentId,@RequestAttribute("userId")Long userId)
            throws ResourceNotFoundException, UnauthorizedException {
        User user= GeneralQueryRepository.getByID(userRepository, userId, USER_NOT_FOUND);
        Comment comment= GeneralQueryRepository.getByID(commentRepository, commentId,COMMENT_NOT_FOUND);
        Set<Comment> replies = comment.getReplies();
        if(!user.equals(comment.getUser()))
            throw new UnauthorizedException("User Do no have permission to delete this Comment");
        commentRepository.deleteInBatch(replies);
        commentRepository.deleteInBatch(Arrays.asList(comment));
        return new ResponseEntity<>(new Message("Comment Deleted Successfully"),HttpStatus.OK);
    }
}
