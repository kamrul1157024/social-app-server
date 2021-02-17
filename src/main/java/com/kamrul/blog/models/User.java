package com.kamrul.blog.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Entity
@Table(
        name = "user_info",
        uniqueConstraints = {
                @UniqueConstraint(name = "unique_user_name",columnNames = "user_name"),
                @UniqueConstraint(name = "unique_email",columnNames = "email")
        }
)
public class User {

    @Id
    @SequenceGenerator(
            name = "user_id_generator",
            sequenceName = "user_id_generator",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "user_id_generator"
    )
    @Column(name = "user_id", updatable = false)
    private Long userId;

    @Column(name = "user_name", nullable = false)
    private String userName;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Temporal(value = TemporalType.DATE)
    @Column(name = "date_of_birth",nullable = false)
    private Date dateOfBirth;

    @Column(name = "is_email_verified",nullable = false)
    private Boolean isEmailVerified;

    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<Post> posts;

    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<Comment> comments;

    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<CommentReply> commentReplies;

    public User(String userName, String firstName, String lastName, String email, String password,Date dateOfBirth) {
        this.userName = userName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.dateOfBirth=dateOfBirth;

        this.isEmailVerified=false;

    }

    public User() {
        this.isEmailVerified=false;
    }


    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @JsonBackReference
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @JsonBackReference
    public List<Post> getPosts() {
        return posts;
    }


    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }

    @JsonBackReference
    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    @JsonBackReference
    public List<CommentReply> getCommentReplies() {
        return commentReplies;
    }

    public void setCommentReplies(List<CommentReply> commentReplies) {
        this.commentReplies = commentReplies;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Boolean getEmailVerified() {
        return isEmailVerified;
    }

    public void setEmailVerified(Boolean emailVerified) {
        isEmailVerified = emailVerified;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(userId, user.userId) && Objects.equals(userName, user.userName) && Objects.equals(firstName, user.firstName) && Objects.equals(lastName, user.lastName) && Objects.equals(email, user.email) && Objects.equals(password, user.password) && Objects.equals(posts, user.posts);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, userName, firstName, lastName, email, password, posts);
    }
}

