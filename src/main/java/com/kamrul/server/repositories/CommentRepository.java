package com.kamrul.server.repositories;

import com.kamrul.server.models.comment.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment,Long> {

    @Query("SELECT new Comment (c.commentId,c.commentText,c.creationDate,c.creator)FROM Comment c WHERE  c.post.postId=:postId ORDER BY c.creationDate DESC")
    List<Comment> getCommentsByPostID(@Param("postId") Long postId);

}
