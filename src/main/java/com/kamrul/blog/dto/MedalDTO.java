package com.kamrul.blog.dto;

import com.kamrul.blog.models.MedalType;

public class MedalDTO {

    private Long postId;
    private Long medalId;
    private MedalType medalType;

    public MedalDTO(Long medalId, MedalType medalType)
    {
        this.medalId=medalId;
        this.medalType=medalType;
    }

    public Long getMedalId() {
        return medalId;
    }

    public void setMedalId(Long medalId) {
        this.medalId = medalId;
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
