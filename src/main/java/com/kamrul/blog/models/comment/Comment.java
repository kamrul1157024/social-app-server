package com.kamrul.blog.models.comment;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.kamrul.blog.configuration.Verifiable;
import com.kamrul.blog.models.post.Post;
import com.kamrul.blog.models.user.User;
import lombok.*;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name = "comments")
public class Comment implements Verifiable {

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

    @JsonBackReference("comment_post")
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

}
