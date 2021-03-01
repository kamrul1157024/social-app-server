package com.kamrul.blog.repositories;

import com.kamrul.blog.models.CommentReply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentReplyRepository extends JpaRepository<CommentReply,Long> {


    @Query("SELECT new CommentReply (cr.commentReplyId,cr.commentReplyText,cr.creationDate,cr.user) FROM CommentReply cr WHERE cr.comment.commentId=:commentId ORDER BY cr.creationDate")
    List<CommentReply> getCommentRepliesByCommentId(@Param("commentId") Long commentId);

}
