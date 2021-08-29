package com.kamrul.server.models.medal;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.kamrul.server.models.compositeKey.UserAndPostCompositeKey;
import com.kamrul.server.models.post.Post;
import com.kamrul.server.models.user.User;
import lombok.*;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
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
}
