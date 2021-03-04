package com.kamrul.blog.models;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(
        name = "user_info",
        uniqueConstraints = {
                @UniqueConstraint(name = "unique_user_name",columnNames = "user_name"),
                @UniqueConstraint(name = "unique_email",columnNames = "email")
        }
)
public class User implements Comparable<User> {

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

    @Column(name = "user_profile_picture")
    private String profilePicture;

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

    @Column(name = "is_email_visible" ,nullable = false)
    private Boolean isEmailVisible;

    @Column(name = "city_name",nullable = false)
    private String city;

    @Column(name = "country_name",nullable = false)
    private String country;

    @Column(name = "gender",nullable = false)
    private String gender;

    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<Post> posts;
    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<Comment> comments;

    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<CommentReply> commentReplies;

    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<Medal> medals;

    @Column(name = "total_number_of_follower",nullable = false)
    private Long totalNumberOfFollower;


    @ManyToMany
    @JoinTable(
            name = "follow",
            joinColumns = @JoinColumn(name = "followed_user_id"),
            inverseJoinColumns = @JoinColumn(name = "followed_by_user_id")
    )
    private Set<User> followedBy;

    @ManyToMany
    @JoinTable(
            name = "follow",
            joinColumns = @JoinColumn(name = "followed_by_user_id"),
            inverseJoinColumns = @JoinColumn(name = "followed_user_id")
    )
    private Set<User> followed;

    private void init()
    {
        this.isEmailVisible=false;
        this.isEmailVerified=false;
        this.totalNumberOfFollower=0L;
    }

    public User(String userName, String firstName, String lastName, String email, String password,Date dateOfBirth) {
        this.userName = userName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.dateOfBirth=dateOfBirth;

        init();

    }

    public User() {

        init();
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

    @JsonBackReference("password")
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    @JsonBackReference("user_posts")
    public List<Post> getPosts() {
        return posts;
    }


    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }

    @JsonBackReference("comments")
    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    @JsonBackReference("comment_replies")
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

    @JsonBackReference("followed")
    public Set<User> getFollowed() {
        return followed;
    }

    public void setFollowed(Set<User> followed) {
        this.followed = followed;
    }

    @JsonBackReference("followedBy")
    public Set<User> getFollowedBy() {
        return followedBy;
    }

    public void setFollowedBy(Set<User> followedBy) {
        this.followedBy = followedBy;
    }

    public Long getTotalNumberOfFollower() {
        return totalNumberOfFollower;
    }

    public void setTotalNumberOfFollower(Long totalNumberOfFollower) {
        this.totalNumberOfFollower = totalNumberOfFollower;
    }

    @JsonBackReference
    public List<Medal> getMedals() {
        return medals;
    }

    public void setMedals(List<Medal> medals) {
        this.medals = medals;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(userId, user.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, userName, profilePicture, firstName, lastName, email, city, country, gender, totalNumberOfFollower);
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    @Override
    public int compareTo(User o) {
        long diff=this.userId-o.getUserId();
        if(diff<0) return -1;
        else if(diff>0) return 1;
        else return 0;
    }
}


