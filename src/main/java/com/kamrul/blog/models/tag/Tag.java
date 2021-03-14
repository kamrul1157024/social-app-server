package com.kamrul.blog.models.tag;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kamrul.blog.models.post.Post;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "tag")
public class Tag {
    @Id
    @SequenceGenerator(
            name = "tag_id_generator",
            sequenceName = "tag_id_generator",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "tag_id_generator"
    )
    @Column(name = "tag_id", updatable = false)
    private Long tagId;
    @Column(name = "tag_name",nullable = false,unique = true)
    private String tagName;

    @ManyToMany(mappedBy = "tags",fetch = FetchType.LAZY,cascade = CascadeType.MERGE)
    @JsonIgnore
    private List<Post> posts;


    public Tag(String tagName) {
        this.tagName = tagName;
    }

    public Tag() {
    }

    public Long getTagId() {
        return tagId;
    }

    public void setTagId(Long postTagId) {
        this.tagId = postTagId;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }
}
