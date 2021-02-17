package com.kamrul.blog.dto;

public class PostDTO {

    private Long postId;
    private String postText;
    private String postTitle;

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

    public String getPostTitle() {
        return postTitle;
    }

    public void setPostTitle(String postTitle) {
        this.postTitle = postTitle;
    }

    @Override
    public String toString() {
        return "PostDTO{" +
                "postId=" + postId +
                ", postText='" + postText + '\'' +
                ", postTitle='" + postTitle + '\'' +
                '}';
    }
}
