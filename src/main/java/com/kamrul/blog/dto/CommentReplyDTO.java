package com.kamrul.blog.dto;

import lombok.*;

@Data
public class CommentReplyDTO {

    private Long commentId;
    private Long commentReplyId;
    private String commentReplyText;

    public CommentReplyDTO(Long userId, Long commentId, Long commentReplyId, String commentText) {
        this.commentId = commentId;
        this.commentReplyId = commentReplyId;
        this.commentReplyText = commentText;
    }
}
