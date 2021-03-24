package com.kamrul.blog.models.compositeKey;


import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Data
@Embeddable
@NoArgsConstructor
public class UserAndPostCompositeKey implements Serializable {

    private Long userId;
    private Long postId;

    public UserAndPostCompositeKey(Long userId, Long postId) {
        this.postId = postId;
        this.userId = userId;
    }

}
