package com.kamrul.blog.models.savedPost;

import com.kamrul.blog.models.compositeKey.UserAndPostCompositeKey;
import com.kamrul.blog.models.post.Post;
import com.kamrul.blog.models.user.User;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "saved_post_by_user")
public class SavedPost {


    @EmbeddedId
    UserAndPostCompositeKey userAndPostCompositeKey;

    @MapsId("userId")
    @ManyToOne
    @JoinColumn
    private User user;

    @MapsId("postId")
    @ManyToOne
    @JoinColumn
    private Post post;


    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "creation_time",nullable = false)
    private Date creationTime;


    public SavedPost(UserAndPostCompositeKey userAndPostCompositeKey,User user, Post post) {
        this.user=user;
        this.post=post;
        this.userAndPostCompositeKey=userAndPostCompositeKey;
        creationTime=new Date();
    }

    public SavedPost() {
        creationTime=new Date();
    }



    public UserAndPostCompositeKey getUserAndPostCompositeKey() {
        return userAndPostCompositeKey;
    }

    public void setUserAndPostCompositeKey(UserAndPostCompositeKey userAndPostCompositeKey) {
        this.userAndPostCompositeKey = userAndPostCompositeKey;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public Date getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SavedPost savedPost = (SavedPost) o;
        return getUserAndPostCompositeKey().equals(savedPost.getUserAndPostCompositeKey()) && getUser().equals(savedPost.getUser()) && getPost().equals(savedPost.getPost()) && getCreationTime().equals(savedPost.getCreationTime());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUserAndPostCompositeKey(), getUser(), getPost(), getCreationTime());
    }
}
