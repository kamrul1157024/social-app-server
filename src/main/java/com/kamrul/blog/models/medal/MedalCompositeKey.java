package com.kamrul.blog.models.medal;


import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class MedalCompositeKey implements Serializable {

    private Long postId;
    private Long userId;

    public MedalCompositeKey(Long userId,Long postId) {
        this.postId = postId;
        this.userId = userId;
    }

    public MedalCompositeKey() {
    }

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MedalCompositeKey that = (MedalCompositeKey) o;
        return Objects.equals(postId, that.postId) && Objects.equals(userId, that.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(postId, userId);
    }

    @Override
    public String toString() {
        return "MedalCompositeKey{" +
                "postId=" + postId +
                ", userId=" + userId +
                '}';
    }
}
