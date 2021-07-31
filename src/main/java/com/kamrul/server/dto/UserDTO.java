package com.kamrul.server.dto;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

import java.util.Date;

@Data
public class UserDTO {
    private Long userId;
    private String userName;
    private String firstName;
    private String lastName;
    @JsonBackReference(value = "user_password")
    private String password;
    private String email;
    private Date dateOfBirth;
    private Boolean isEmailVerified;
    private String profilePicture;
    private Boolean isEmailVisible;
    private String city;
    private String country;
    private String gender;
    private Long totalNumberOfFollower;
    private Boolean isFollowedByCurrentlyLoggedInUser;
    private Long totalNumberOfUserFollowed;
    private String userDescription;
    private Boolean deleted;

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth=dateOfBirth;
    }

    public Boolean getEmailVisible() {
        return isEmailVisible;
    }

    public void setEmailVisible(Boolean emailVisible) {
        isEmailVisible = emailVisible;
    }

    public Boolean getEmailVerified() {
        return isEmailVerified;
    }

    public void setEmailVerified(Boolean emailVerified) {
        isEmailVerified = emailVerified;
    }

    public Boolean getFollowedByCurrentlyLoggedInUser() {
        return isFollowedByCurrentlyLoggedInUser;
    }

    public void setFollowedByCurrentlyLoggedInUser(Boolean followedByCurrentlyLoggedInUser) {
        isFollowedByCurrentlyLoggedInUser = followedByCurrentlyLoggedInUser;
    }

}
