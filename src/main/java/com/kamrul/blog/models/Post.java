package com.kamrul.blog.models;

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
    private Date creationTime;

    @Column(name = "post_text",nullable = false,columnDefinition = "TEXT")
    private String postText;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id",nullable = false)
    private User user;

    @OneToMany(mappedBy = "post",fetch = FetchType.EAGER)
    private List<Comment> comments;


    public Post(String postText) {
        this.postText = postText;
        this.creationTime=new Date();
    }

    public Post() {
        this.creationTime=new Date();
    }

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public Date getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
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
}
