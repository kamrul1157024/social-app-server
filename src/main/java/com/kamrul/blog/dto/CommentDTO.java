package com.kamrul.blog.dto;

import lombok.*;

@Data
public class CommentDTO {
    private Long postId;
    private Long commentId;
    private String commentText;

    public CommentDTO(Long postId, Long commentId, String commentText) {
        this.postId = postId;
        this.commentId = commentId;
        this.commentText = commentText;
    }
}
