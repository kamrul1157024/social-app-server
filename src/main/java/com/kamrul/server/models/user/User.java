package com.kamrul.server.models.user;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.kamrul.server.models.booklet.Booklet;
import com.kamrul.server.models.comment.Comment;
import com.kamrul.server.models.comment.CommentReply;
import com.kamrul.server.models.community.Community;
import com.kamrul.server.models.medal.Medal;
import com.kamrul.server.models.post.Post;
import com.kamrul.server.models.savedPost.SavedPost;
import lombok.*;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Set;

@EqualsAndHashCode
@Data
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

    @JsonBackReference("password")
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
    @Column(name = "total_number_of_follower",nullable = false)
    private Long totalNumberOfFollower;

    @Column(name = "total_number_of_user_followed",nullable = false)
    private Long totalNumberOfUserFollowed;

    @Column(name = "user_description",columnDefinition = "Text")
    private String userDescription;

    @Column(name = "deleted")
    private Boolean deleted = false;

    @JsonBackReference("user_posts")
    @OneToMany(mappedBy = "user",cascade = CascadeType.REMOVE,fetch = FetchType.LAZY)
    private List<Post> posts;

    @JsonBackReference("comments")
    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<Comment> comments;

    @JsonBackReference("comment_replies")
    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<CommentReply> commentReplies;

    @JsonBackReference("user_medal")
    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<Medal> medals;

    @JsonBackReference("user_booklet")
    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<Booklet> booklets;


    @JsonBackReference("followedBy")
    @ManyToMany
    @JoinTable(
            name = "follow",
            joinColumns = @JoinColumn(name = "followed_user_id"),
            inverseJoinColumns = @JoinColumn(name = "followed_by_user_id")
    )
    private Set<User> followedBy;

    @JsonBackReference("followed")
    @ManyToMany
    @JoinTable(
            name = "follow",
            joinColumns = @JoinColumn(name = "followed_by_user_id"),
            inverseJoinColumns = @JoinColumn(name = "followed_user_id")
    )
    private Set<User> followed;


    @JsonBackReference
    @OneToMany(mappedBy = "user",fetch = FetchType.LAZY)
    private Set<SavedPost> savedPosts;

    @OneToMany(mappedBy = "owner",fetch = FetchType.LAZY)
    @JsonBackReference("communities")
    private Set<Community> communities;

    private void init() {
        this.isEmailVisible=false;
        this.isEmailVerified=false;
        this.totalNumberOfFollower=0L;
        this.totalNumberOfUserFollowed=0L;
    }

    public User(String userName, String firstName, String lastName, String email, String password, Date dateOfBirth) {
        this.userName = userName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.dateOfBirth=dateOfBirth;
        init();
    }

    public User() { init(); }

    public Boolean getEmailVerified() { return isEmailVerified; }
    public void setEmailVerified(Boolean emailVerified) { isEmailVerified = emailVerified; }
    public Boolean getEmailVisible() { return isEmailVisible; }
    public void setEmailVisible(Boolean emailVisible) { isEmailVisible = emailVisible; }

    @Override
    public int compareTo(User o) {
        long diff=this.userId-o.getUserId();
        if(diff<0) return -1;
        else if(diff>0) return 1;
        else return 0;
    }



}


