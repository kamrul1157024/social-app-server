package com.kamrul.blog.dto;

import com.kamrul.blog.models.medal.MedalType;
import com.kamrul.blog.models.tag.Tag;
import com.kamrul.blog.models.user.User;

import java.util.Collection;
import java.util.Date;
import java.util.List;

public class PostDTO {

    private Long postId;
    private String postText;
    private String postTitle;
    private Boolean isDraft;
    private Date creationDate;
    private User user;
    private Long totalBronze;
    private Long totalSilver;
    private Long totalGold;
    private List<Tag> tags;
    private MedalType medalTypeProvidedByLoggedInUser;
    public PostDTO(
            Long postId,
            String postText,
            String postTitle,
            Boolean isDraft,
            Date creationDate,
            User user,
            Long totalBronze,
            Long totalSilver,
            Long totalGold,
            Collection<Tag> tags
    ) {
        this.postId = postId;
        this.postText = postText;
        this.postTitle = postTitle;
        this.isDraft = isDraft;
        this.creationDate = creationDate;
        this.user = user;
        this.totalBronze = totalBronze;
        this.totalSilver = totalSilver;
        this.totalGold = totalGold;
        this.tags = (List<Tag>) tags;
    }

    public PostDTO(){};

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

    public void setUser(User user) {
        this.user = user;
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

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public Long getTotalBronze() {
        return totalBronze;
    }

    public void setTotalBronze(Long totalBronze) {
        this.totalBronze = totalBronze;
    }

    public Long getTotalSilver() {
        return totalSilver;
    }

    public void setTotalSilver(Long totalSilver) {
        this.totalSilver = totalSilver;
    }

    public Long getTotalGold() {
        return totalGold;
    }

    public void setTotalGold(Long totalGold) {
        this.totalGold = totalGold;
    }

    public MedalType getMedalTypeProvidedByLoggedInUser() {
        return medalTypeProvidedByLoggedInUser;
    }

    public void setMedalTypeProvidedByLoggedInUser(MedalType medalTypeProvidedByLoggedInUser) {
        this.medalTypeProvidedByLoggedInUser = medalTypeProvidedByLoggedInUser;
    }
}
