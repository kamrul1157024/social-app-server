package com.kamrul.blog.models.compositeKey;


import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class UserAndPostCompositeKey implements Serializable {

    @Column(name = "user_id")
    private Long userId;
    @Column(name = "post_id")
    private Long postId;

    public UserAndPostCompositeKey(Long userId, Long postId) {
        this.postId = postId;
        this.userId = userId;
    }

    public UserAndPostCompositeKey() {
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
        UserAndPostCompositeKey that = (UserAndPostCompositeKey) o;
        return Objects.equals(postId, that.postId) && Objects.equals(userId, that.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(postId, userId);
    }

    @Override
    public String toString() {
        return "UserAndPostCompositeKey{" +
                "postId=" + postId +
                ", userId=" + userId +
                '}';
    }
}
