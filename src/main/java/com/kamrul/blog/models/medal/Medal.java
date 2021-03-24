package com.kamrul.blog.models.medal;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.kamrul.blog.models.compositeKey.UserAndPostCompositeKey;
import com.kamrul.blog.models.post.Post;
import com.kamrul.blog.models.user.User;
import lombok.*;

import javax.persistence.*;
import java.util.Objects;

@Data
@EqualsAndHashCode
@Entity
@Table(name = "medal")
public class Medal {

    @EmbeddedId
    UserAndPostCompositeKey userAndPostCompositeKey;

    @JsonBackReference("medal_user")
    @MapsId("userId")
    @ManyToOne
    @JoinColumn
    private User user;

    @JsonBackReference("medal_post")
    @MapsId("postId")
    @ManyToOne
    @JoinColumn
    private Post post;

    @Column(name = "medal_type")
    MedalType medalType;

    public Medal(UserAndPostCompositeKey userAndPostCompositeKey, User user, Post post, MedalType medalType) {
        this.userAndPostCompositeKey = userAndPostCompositeKey;
        this.user = user;
        this.post = post;
        this.medalType = medalType;
    }


    public Medal() {
    }

}
