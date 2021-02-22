package com.kamrul.blog.models;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

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

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id",nullable = false)
    private User user;

    @OneToMany(mappedBy = "post",fetch = FetchType.EAGER)
    private List<Comment> comments;

    @OneToMany(mappedBy = "post",fetch = FetchType.LAZY)
    private List<UpVote> upVotes;

    @Column(name = "total_up_votes")
    private Long totalUpVotes;


    private void init()
    {
        this.creationDate =new Date();
        this.isDraft=false;
        totalUpVotes=0L;
    }


    public Post(String postText) {
        this.postText = postText;
        init();
    }

    public Post() {
        init();

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

    public void setCreationDate(Date creationTime) {
        this.creationDate = creationTime;
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

    @JsonBackReference("upvoter_list")
    public List<UpVote> getUpVotes() {
        return upVotes;
    }

    public void setUpVotes(List<UpVote> upVotes) {
        this.upVotes = upVotes;
    }

    public Long getTotalUpVotes() {
        return totalUpVotes;
    }

    public void setTotalUpVotes(Long totalUpVotes) {
        this.totalUpVotes = totalUpVotes;
    }

    public boolean isDraft() {
        return isDraft;
    }

    public void setDraft(boolean draft) {
        isDraft = draft;
    }
}
