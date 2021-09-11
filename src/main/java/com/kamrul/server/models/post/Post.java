package com.kamrul.server.models.post;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kamrul.server.models.savedPost.SavedPost;
import com.kamrul.server.models.tag.Tag;
import com.kamrul.server.models.user.User;
import com.kamrul.server.models.medal.Medal;
import com.kamrul.server.models.medal.MedalType;
import lombok.*;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Set;

@EqualsAndHashCode
@Data
@Entity
@Table(name = "post")
public class Post {


    @Id
    @SequenceGenerator(
            name = "post_id_generator",
            sequenceName = "post_id_generator",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "post_id_generator"
    )
    @Column(name = "post_id", updatable = false)
    private Long postId;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "creation_time",nullable = false)
    private Date creationDate;

    @Column(name = "post_title",nullable = false,columnDefinition = "TEXT")
    private String postTitle;

    @Column(name = "post_text",nullable = false,columnDefinition = "TEXT")
    private String postText;

    @Column(name = "is_draft",nullable = false)
    private boolean isDraft;

    @Column(name = "total_bronze")
    private Long totalBronze;
    @Column(name = "total_silver")
    private Long totalSilver;
    @Column(name = "total_gold")
    private Long totalGold;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id",nullable = false)
    private User user;

    @JsonBackReference
    @OneToMany(mappedBy = "post",fetch = FetchType.LAZY)
    private List<Medal> medals;

    @OneToMany(mappedBy = "post",fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<SavedPost> savedPosts;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "post_tags",
            joinColumns = {@JoinColumn(name = "post_id")},
            inverseJoinColumns =  {@JoinColumn(name = "tag_id")}
    )
    private List<Tag> tags;

    private void init() {
        if(this.postId==null)
            this.creationDate = new Date();
        this.isDraft=false;
        this.totalBronze=0L;
        this.totalSilver=0L;
        this.totalGold=0L;
    }


    public Post(String postText) {
        this.postText = postText;
        init();
    }

    public Post() {
        init();
    }


    public void addMedalCount(MedalType medalType)
    {
        if(medalType==MedalType.BRONZE) this.totalBronze++;
        if(medalType==MedalType.SILVER) this.totalSilver++;
        if(medalType==MedalType.GOLD) this.totalGold++;
    }

    public void removePreviousMedalCount(MedalType previousMedalType)
    {
        if(previousMedalType==MedalType.BRONZE) this.totalBronze--;
        if(previousMedalType==MedalType.SILVER) this.totalSilver--;
        if(previousMedalType==MedalType.GOLD) this.totalGold--;
    }

    public boolean getDraft() {
        return isDraft;
    }

    public void setDraft(boolean draft) {
        isDraft = draft;
    }

    @Override
    public String toString() {
        return "Post{" +
                "postId=" + postId +
                ", creationDate=" + creationDate +
                ", postTitle='" + postTitle + '\'' +
                ", postText='" + postText + '\'' +
                ", isDraft=" + isDraft +
                ", totalBronze=" + totalBronze +
                ", totalSilver=" + totalSilver +
                ", totalGold=" + totalGold +
                '}';
    }
}
