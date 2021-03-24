package com.kamrul.blog.dto;

import com.kamrul.blog.models.medal.MedalType;
import com.kamrul.blog.models.tag.Tag;
import com.kamrul.blog.models.user.User;
import lombok.*;

import java.util.Collection;
import java.util.Date;
import java.util.List;

@Data
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

    public Boolean getDraft() {
        return isDraft;
    }

    public void setDraft(Boolean draft) {
        isDraft = draft;
    }

}
