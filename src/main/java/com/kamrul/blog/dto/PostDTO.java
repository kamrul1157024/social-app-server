package com.kamrul.blog.dto;

import com.kamrul.blog.models.Comment;
import com.kamrul.blog.models.User;

import java.util.Date;
import java.util.List;

public class PostDTO {

    private Long postId;
    private String postText;
    private String postTitle;
    private Boolean isDraft;
    private Date creationDate;
    private List<Comment> comments;
    private User user;
    private Boolean isPostUpVotedByCurrentUser;
    private Long totalUpVotes;

    public  PostDTO()
    {
        isPostUpVotedByCurrentUser=false;
    }

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

    public Boolean getPostUpVotedByCurrentUser() {
        return isPostUpVotedByCurrentUser;
    }

    public void setPostUpVotedByCurrentUser(Boolean postUpVotedByCurrentUser) {
        isPostUpVotedByCurrentUser = postUpVotedByCurrentUser;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public User getUser() {
        return user;
    }

    public Boolean getDraft() {
        return isDraft;
    }

    public void setDraft(Boolean draft) {
        isDraft = draft;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Long getTotalUpVotes() {
        return totalUpVotes;
    }

    public void setTotalUpVotes(Long totalUpVotes) {
        this.totalUpVotes = totalUpVotes;
    }
}
