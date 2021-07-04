package com.kamrul.server.dto;


import lombok.*;

@Data
public class SavedPostDTO {

    private Long postId;
    private Boolean isSavedByLoggedInUser;

    public SavedPostDTO(Long postId, Boolean isSavedByLoggedInUser) {
        this.postId = postId;
        this.isSavedByLoggedInUser = isSavedByLoggedInUser;
    }

    public Boolean getSavedByLoggedInUser() {
        return isSavedByLoggedInUser;
    }

    public void setSavedByLoggedInUser(Boolean savedByLoggedInUser) {
        isSavedByLoggedInUser = savedByLoggedInUser;
    }
}
