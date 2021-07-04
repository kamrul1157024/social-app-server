package com.kamrul.server.models.tag;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kamrul.server.models.post.Post;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Data
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

    @ManyToMany(mappedBy = "tags",fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Post> posts;


    public Tag(String tagName) {
        this.tagName = tagName;
    }

    public Tag() {
    }

}
