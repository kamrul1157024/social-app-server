package com.kamrul.blog.models;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "comment_reply")
public class CommentReply {

    @Id
    @SequenceGenerator(
            name = "comment_reply_id_generator",
            sequenceName = "comment_reply_id_generator",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "comment_reply_id_generator"
    )
    @Column(name = "comment_reply_id",nullable = false,updatable = false)
    private Long commentReplyId;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "creation_date",nullable = false)
    private Date creationDate;

    @Column(name = "comment_reply_text",nullable = false,columnDefinition = "TEXT")
    private String commentReplyText;

    @ManyToOne
    @JoinColumn(name = "comment_id")
    private Comment comment;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public CommentReply(String commentReplyString, Long upVotes, Long downVotes) {
        this.commentReplyText = commentReplyString;
        this.creationDate=new Date();
    }

    public CommentReply() {
        this.creationDate=new Date();
    }

    public Long getCommentReplyId() {
        return commentReplyId;
    }

    public void setCommentReplyId(Long commentReplyId) {
        this.commentReplyId = commentReplyId;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public String getCommentReplyText() {
        return commentReplyText;
    }

    public void setCommentReplyText(String commentReplyText) {
        this.commentReplyText = commentReplyText;
    }

    @JsonBackReference
    public Comment getComment() {
        return comment;
    }

    public void setComment(Comment comment) {
        this.comment = comment;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
