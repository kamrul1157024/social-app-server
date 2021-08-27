package com.kamrul.server.models.comment;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.kamrul.server.services.verify.Verifiable;
import com.kamrul.server.models.user.User;
import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Data
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

    @JsonBackReference("comment_reply_comment")
    @ManyToOne
    @JoinColumn(name = "comment_id")
    private Comment comment;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id",nullable = false,referencedColumnName = "user_id")
    private User creator;

    private void init()
    {
        this.creationDate=new Date();
    }

    public CommentReply(String commentReplyString) {
        this.commentReplyText = commentReplyString;
        init();
    }

    public CommentReply(Long commentReplyId,String commentReplyText,Date creationDate,User creator)
    {
        this.commentReplyId=commentReplyId;
        this.commentReplyText=commentReplyText;
        this.creationDate=creationDate;
        this.creator=creator;
    }


    public CommentReply() {
        init();
    }

}
