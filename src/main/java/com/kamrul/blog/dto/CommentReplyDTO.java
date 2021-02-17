package com.kamrul.blog.dto;

public class CommentReplyDTO {

    private Long commentId;
    private Long commentReplyId;
    private String commentReplyText;

    public CommentReplyDTO(Long userId, Long commentId, Long commentReplyId, String commentText) {
        this.commentId = commentId;
        this.commentReplyId = commentReplyId;
        this.commentReplyText = commentText;
    }

    public Long getCommentId() {
        return commentId;
    }

    public void setCommentId(Long commentId) {
        this.commentId = commentId;
    }

    public Long getCommentReplyId() {
        return commentReplyId;
    }

    public void setCommentReplyId(Long commentReplyId) {
        this.commentReplyId = commentReplyId;
    }

    public String getCommentReplyText() {
        return commentReplyText;
    }

    public void setCommentReplyText(String commentReplyText) {
        this.commentReplyText = commentReplyText;
    }
}
