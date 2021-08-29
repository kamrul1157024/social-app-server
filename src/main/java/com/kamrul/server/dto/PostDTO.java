package com.kamrul.server.dto;

import com.kamrul.server.models.medal.MedalType;
import com.kamrul.server.models.post.Post;
import com.kamrul.server.models.tag.Tag;
import com.kamrul.server.models.user.User;
import com.kamrul.server.services.verify.Verifiable;
import lombok.*;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostDTO implements Verifiable {
    public Long postId;
    public String postText;
    public String postTitle;
    public Boolean draft;
    public Date creationDate;
    public User user;
    public Long totalBronze;
    public Long totalSilver;
    public Long totalGold;
    public List<Tag> tags;
    public MedalType medalTypeProvidedByLoggedInUser;

    public PostDTO(Post post){
        this.user = post.getUser();
        this.postId = post.getPostId();
        this.postTitle = post.getPostTitle();
        this.postText = post.getPostText();
        this.creationDate = post.getCreationDate();
        this.draft = post.getDraft();
        this.totalBronze = post.getTotalBronze();
        this.totalSilver = post.getTotalSilver();
        this.totalGold = post.getTotalGold();
        this.tags = post.getTags();
        this.user = post.getUser();
    }

    public PostDTO(Post post,MedalType medalType){
        this(post);
        if(medalType!=null)
            this.medalTypeProvidedByLoggedInUser = medalType;
        else
            this.medalTypeProvidedByLoggedInUser = MedalType.NO_MEDAL;

    }
}
