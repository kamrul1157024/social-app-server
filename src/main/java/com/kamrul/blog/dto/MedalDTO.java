package com.kamrul.blog.dto;

import com.kamrul.blog.models.MedalType;

public class MedalDTO {

    private Long userId;
    private Long postId;
    private MedalType medalType;

    public MedalDTO(Long userId, Long postId, MedalType medalType) {
        this.userId = userId;
        this.postId = postId;
        this.medalType = medalType;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public MedalType getMedalType() {
        return medalType;
    }

    public void setMedalType(MedalType medalType) {
        this.medalType = medalType;
    }
}
