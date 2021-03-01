package com.kamrul.blog.dto;

public class CommentDTO {
    private Long postId;
    private Long commentId;
    private String commentText;

    public CommentDTO(Long postId, Long commentId, String commentText) {
        this.postId = postId;
        this.commentId = commentId;
        this.commentText = commentText;
    }

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public Long getCommentId() {
        return commentId;
    }

    public void setCommentId(Long commentId) {
        this.commentId = commentId;
    }

    public String getCommentText() {
        return commentText;
    }

    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }
}
