package com.kamrul.blog.models.medal;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.kamrul.blog.models.compositeKey.UserAndPostCompositeKey;
import com.kamrul.blog.models.post.Post;
import com.kamrul.blog.models.user.User;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "medal")
public class Medal {

    @EmbeddedId
    UserAndPostCompositeKey id;
    @MapsId("userId")
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @MapsId("postId")
    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @Column(name = "medal_type")
    MedalType medalType;

    public Medal(UserAndPostCompositeKey id, User user, Post post, MedalType medalType) {
        this.id = id;
        this.user = user;
        this.post = post;
        this.medalType = medalType;
    }


    public Medal() {
    }

    public UserAndPostCompositeKey getId() {
        return id;
    }

    public void setId(UserAndPostCompositeKey id) {
        this.id = id;
    }

    @JsonBackReference("medal_user")
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @JsonBackReference("medal_post")
    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public MedalType getMedalType() {
        return medalType;
    }

    public void setMedalType(MedalType medalType) {
        this.medalType = medalType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Medal medal = (Medal) o;
        return getId().equals(medal.getId()) && getUser().equals(medal.getUser()) && getPost().equals(medal.getPost()) && getMedalType() == medal.getMedalType();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getUser(), getPost(), getMedalType());
    }
}
