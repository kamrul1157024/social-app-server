package com.kamrul.blog.dto;

public class PostDTO {

    private Long postId;
    private String postText;

    public String getPostText() {
        return postText;
    }

    public void setPostText(String postText) {
        this.postText = postText;
    }

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    @Override
    public String toString() {
        return "PostDTO{" +
                ", postText='" + postText + '\'' +
                '}';
    }
}
