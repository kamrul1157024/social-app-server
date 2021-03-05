package com.kamrul.blog.dto;

import com.fasterxml.jackson.annotation.JsonBackReference;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UserDTO {
    private Long userId;
    private String userName;
    private String firstName;
    private String lastName;
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

    public UserDTO(){}

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @JsonBackReference(value = "user_password")
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }



    public void setDateOfBirth(String dateOfBirth) {
        try {
            this.dateOfBirth = new SimpleDateFormat("dd/MM/yyyy").parse(dateOfBirth);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth=dateOfBirth;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public Boolean getEmailVisible() {
        return isEmailVisible;
    }

    public void setEmailVisible(Boolean emailVisible) {
        isEmailVisible = emailVisible;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
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

    public Long getTotalNumberOfFollower() {
        return totalNumberOfFollower;
    }

    public void setTotalNumberOfFollower(Long totalNumberOfFollower) {
        this.totalNumberOfFollower = totalNumberOfFollower;
    }

    public Long getTotalNumberOfUserFollowed() {
        return totalNumberOfUserFollowed;
    }

    public void setTotalNumberOfUserFollowed(Long totalNumberOfUserFollowed) {
        this.totalNumberOfUserFollowed = totalNumberOfUserFollowed;
    }

    public String getUserDescription() {
        return userDescription;
    }

    public void setUserDescription(String userDescription) {
        this.userDescription = userDescription;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
