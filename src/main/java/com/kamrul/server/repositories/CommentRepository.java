package com.kamrul.server.repositories;

import com.kamrul.server.models.comment.Comment;
import com.kamrul.server.models.comment.CommentForType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CommentRepository extends JpaRepository<Comment,Long> {
    @Query("SELECT c FROM Comment c WHERE c.commentForType=:commentForType AND c.commentFor=:commentFor ORDER BY c.creationDate DESC ")
    Page<Comment> getCommentsByCommentForTypeAndAndCommentFor(
            @Param("commentForType") CommentForType commentForType,
            @Param("commentFor") Long commentFor,
            Pageable pageable
    );
}
