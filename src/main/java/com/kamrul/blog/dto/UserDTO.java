package com.kamrul.blog.dto;

import com.fasterxml.jackson.annotation.JsonBackReference;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UserDTO {
    private String username;
    private String firstName;
    private String lastName;
    private String password;
    private String email;
    private Date dateOfBirth;
    private Boolean isEmailVerified;

    public UserDTO(){}

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    @JsonBackReference
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


    public Boolean getEmailVerified() {
        return isEmailVerified;
    }

    public void setIsEmailVerified(Boolean isEmailVerified) {
        this.isEmailVerified = isEmailVerified;
    }
}
