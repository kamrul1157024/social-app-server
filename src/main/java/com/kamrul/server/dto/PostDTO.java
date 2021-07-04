package com.kamrul.server.dto;

import com.kamrul.server.models.medal.MedalType;
import com.kamrul.server.models.tag.Tag;
import com.kamrul.server.models.user.User;
import com.kamrul.server.services.verify.Verifiable;
import lombok.*;

import java.util.Collection;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostDTO implements Verifiable {
    public Long postId;
    public String postText;
    public String postTitle;
    public Boolean isDraft;
    public Date creationDate;
    public User user;
    public Long totalBronze;
    public Long totalSilver;
    public Long totalGold;
    public List<Tag> tags;
    public MedalType medalTypeProvidedByLoggedInUser;
    public Boolean getDraft() {
        return isDraft;
    }
    public void setDraft(Boolean draft) {
        isDraft = draft;
    }
}
