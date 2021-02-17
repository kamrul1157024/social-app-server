package com.kamrul.blog.repositories;

import com.kamrul.blog.models.CommentReply;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentReplyRepository extends JpaRepository<CommentReply,Long> {



}
