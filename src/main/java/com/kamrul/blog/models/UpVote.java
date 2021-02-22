package com.kamrul.blog.models;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;

@Entity
@Table(name = "upvote")
public class UpVote {



    @Id
    @SequenceGenerator(
            name = "upvote_id_generator",
            sequenceName = "upvote_id_generator",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "upvote_id_generator"
    )
    @Column(name = "upvote_id",updatable = false)
    private Long upVoteId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

            public UpVote(User user, Post post) {
        this.user = user;
        this.post = post;
    }

    public UpVote()
    {

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


}
