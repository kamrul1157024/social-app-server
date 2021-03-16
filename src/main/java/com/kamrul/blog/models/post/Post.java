package com.kamrul.blog.models.post;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.kamrul.blog.configuration.Verifiable;
import com.kamrul.blog.models.tag.Tag;
import com.kamrul.blog.models.user.User;
import com.kamrul.blog.models.comment.Comment;
import com.kamrul.blog.models.medal.Medal;
import com.kamrul.blog.models.medal.MedalType;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "post")
public class Post implements Verifiable {


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

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id",nullable = false)
    private User user;

    @OneToMany(mappedBy = "post",fetch = FetchType.LAZY)
    private List<Comment> comments;

    @OneToMany(mappedBy = "post",fetch = FetchType.LAZY)
    private List<Medal> medals;

    @Column(name = "total_bronze")
    private Long totalBronze;
    @Column(name = "total_silver")
    private Long totalSilver;
    @Column(name = "total_gold")
    private Long totalGold;


    @ManyToMany(cascade =CascadeType.MERGE,fetch = FetchType.EAGER)
    @JoinTable(name = "post_tags",
            joinColumns = {@JoinColumn(name = "post_id")},
            inverseJoinColumns =  {@JoinColumn(name = "tag_id")}
    )
    private List<Tag> tags;

    private void init()
    {
        this.creationDate =new Date();
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

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public String getPostTitle() {
        return postTitle;
    }

    public void setPostTitle(String postTitle) {
        this.postTitle = postTitle;
    }

    public String getPostText() {
        return postText;
    }

    public void setPostText(String postText) {
        this.postText = postText;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    @JsonBackReference
    public List<Medal> getMedals() {
        return medals;
    }

    public void setMedals(List<Medal> medals) {
        this.medals = medals;
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

    public void setTotalSilver(Long totalSiver) {
        this.totalSilver = totalSiver;
    }

    public Long getTotalGold() {
        return totalGold;
    }

    public void setTotalGold(Long totalGold) {
        this.totalGold = totalGold;
    }

    public boolean isDraft() {
        return isDraft;
    }

    public void setDraft(boolean draft) {
        isDraft = draft;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }
}
