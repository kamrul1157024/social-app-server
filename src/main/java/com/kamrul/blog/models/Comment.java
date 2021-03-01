package com.kamrul.blog.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "comments")
public class Comment {

    @Id
    @SequenceGenerator(
            name = "comment_id_generator",
            sequenceName = "comment_id_generator",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "comment_id_generator"
    )
    @Column(name = "comment_id",nullable = false,updatable = false)
    private Long commentId;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "creation_date",nullable = false)
    private Date creationDate;

    @Column(name = "comment_text",nullable = false,columnDefinition = "TEXT")
    private String commentText;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "comment",fetch = FetchType.LAZY)
    private List<CommentReply> commentReplies;

    private void init()
    {
        this.creationDate= new Date();
    }

    public Comment(String commentText, Long upVotes, Long downVotes) {
        this.commentText = commentText;
        init();
    }

    public  Comment(Long commentId,String commentText,Date creationDate,User user)
    {
        this.commentId=commentId;
        this.commentText=commentText;
        this.creationDate=creationDate;
        this.user=user;
    }

    public Comment() {
        init();
    }

    public Long getCommentId() {
        return commentId;
    }

    public void setCommentId(Long commentId) {
        this.commentId = commentId;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public String getCommentText() {
        return commentText;
    }

    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }

    @JsonBackReference("comment_post")
    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<CommentReply> getCommentReplies() {
        return commentReplies;
    }

    public void setCommentReplies(List<CommentReply> commentReplies) {
        this.commentReplies = commentReplies;
    }
}
