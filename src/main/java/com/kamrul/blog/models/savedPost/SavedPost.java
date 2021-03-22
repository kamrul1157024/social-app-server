package com.kamrul.blog.models.savedPost;

import com.kamrul.blog.models.compositeKey.UserAndPostCompositeKey;
import com.kamrul.blog.models.post.Post;
import com.kamrul.blog.models.user.User;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "saved_post_by_user")
@Data
@EqualsAndHashCode
public class SavedPost {


    @EmbeddedId
    UserAndPostCompositeKey userAndPostCompositeKey;

    @ManyToOne
    @MapsId("user_id")
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @MapsId("post_id")
    @JoinColumn(name = "post_id")
    private Post post;


    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "creation_time",nullable = false)
    private Date creationTime;

    public SavedPost(User user, Post post) {
        creationTime=new Date();
    }

    public SavedPost() {
        creationTime=new Date();
    }
}
