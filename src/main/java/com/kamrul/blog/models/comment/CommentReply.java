package com.kamrul.blog.models.comment;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.kamrul.blog.configuration.Verifiable;
import com.kamrul.blog.models.user.User;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "comment_reply")
public class CommentReply implements Verifiable {

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

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    private void init()
    {
        this.creationDate=new Date();
    }

    public CommentReply(String commentReplyString) {
        this.commentReplyText = commentReplyString;
        init();
    }

    public CommentReply(Long commentReplyId,String commentReplyText,Date creationDate,User user)
    {
        this.commentReplyId=commentReplyId;
        this.commentReplyText=commentReplyText;
        this.creationDate=creationDate;
        this.user=user;
    }


    public CommentReply() {
        init();
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

    @JsonBackReference("comment_reply_comment")
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
