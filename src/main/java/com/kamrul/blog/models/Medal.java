package com.kamrul.blog.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.kamrul.blog.dto.MedalDTO;

import javax.persistence.*;

@Entity
@Table(name = "medal")
public class Medal {


    @Id
    @SequenceGenerator(
            name = "medal_id_generator",
            sequenceName = "medal_id_generator",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "medal_id_generator"
    )
    @Column(name = "medal_id",updatable = false)
    private Long medalId;

    @Column(name = "medal_type",nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private MedalType medalType;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    public Medal(User user, Post post, MedalType medalGivenByUser) {
        this.user = user;
        this.post = post;
        this.medalType=medalGivenByUser;
    }

    public Medal(Long medalId,User user,Post post) {
        this.medalId = medalId;
        this.user = user;
        this.post = post;
    }

    public Medal(Medal medal,MedalType medalType)
    {
        this.medalId=medal.getMedalId();
        this.user=medal.getUser();
        this.post=medal.getPost();
        this.medalType=medalType;
    }

    public Medal()
    {

    }

    public Long getMedalId() {
        return medalId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @JsonBackReference("upvote_post")
    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public MedalType getMedalType() {
        return medalType;
    }

    public void setMedalType(MedalType medalType) {
        this.medalType = medalType;
    }
}
