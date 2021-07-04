package com.kamrul.server.models.compositeKey;


import lombok.*;

import javax.persistence.Embeddable;
import java.io.Serializable;

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
