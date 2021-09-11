package com.kamrul.server.models.comment;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.kamrul.server.models.user.User;
import lombok.*;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Data
@NoArgsConstructor
@Entity
@Table(
        name = "comments",
        indexes = {
                @Index(name = "commentForType", columnList = "commentForType"), //default unique=false
                @Index(name = "commentFor",columnList = "commentFor") //default unique=false
        }
)
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
    @CreationTimestamp
    @Column(name = "creation_date", nullable = false)
    private Date creationDate;

    @Column(name = "comment_text",nullable = false,columnDefinition = "TEXT")
    private String commentText;

    @Column(name = "commentForType",nullable = false)
    private CommentForType commentForType;

    @Column(name = "commentFor",nullable = false)
    private Long commentFor;

    @Column(name = "updated",columnDefinition = "boolean default false")
    private Boolean updated;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    @JsonBackReference("replies")
    @OneToMany(mappedBy = "replyToComment", fetch = FetchType.EAGER)
    private Set<Comment> replies;

    @JsonBackReference("replyTo")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reply_to")
    private Comment replyToComment;

    @Column(name = "reply_to",insertable = false,updatable = false)
    private Long replyTo;

    public Comment(User user,String commentText,CommentForType commentForType,Long commentFor){
        this.user = user;
        this.commentText = commentText;
        this.commentForType = commentForType;
        this.commentFor = commentFor;
    }

    public Comment(User user,Comment comment,String replyText){
        this.user = user;
        this.commentForType =  comment.commentForType;
        this.commentFor = comment.commentFor;
        this.commentText = replyText;
        this.replyToComment = comment;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "commentId=" + commentId +
                ", creationDate=" + creationDate +
                ", commentText='" + commentText + '\'' +
                ", commentForType=" + commentForType +
                ", commentFor=" + commentFor +
                ", updated=" + updated +
                ", user=" + user +
                ", replyTo=" + replyTo +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Comment comment = (Comment) o;
        return new EqualsBuilder()
                .append(commentId, comment.commentId)
                .append(creationDate, comment.creationDate)
                .append(commentText, comment.commentText)
                .append(commentForType, comment.commentForType)
                .append(commentFor, comment.commentFor)
                .append(updated, comment.updated)
                .append(user.getUserId(), comment.user.getUserId())
                .append(replyTo, comment.replyTo)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).
                append(commentId)
                .append(creationDate)
                .append(commentText)
                .append(commentForType)
                .append(commentFor)
                .append(updated)
                .append(user.getUserId())
                .append(replyTo)
                .toHashCode();
    }
}
