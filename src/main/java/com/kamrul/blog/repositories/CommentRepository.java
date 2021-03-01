package com.kamrul.blog.repositories;

import com.kamrul.blog.dto.CommentDTO;
import com.kamrul.blog.models.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment,Long> {

    @Query("SELECT new Comment (c.commentId,c.commentText,c.creationDate,c.user)FROM Comment c WHERE  c.post.postId=:postId ORDER BY c.creationDate DESC")
    List<Comment> getCommentsByPostID(@Param("postId") Long postId);

}
