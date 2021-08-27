package com.kamrul.server.models.community;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.kamrul.server.models.post.Post;
import com.kamrul.server.models.user.User;
import lombok.*;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Data
@EqualsAndHashCode
@NoArgsConstructor
@Entity
@Table(name = "community")
public class Community {
    @Id
    @SequenceGenerator(
            name = "community_id_generator",
            sequenceName = "community_id_generator",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "community_id_generator"
    )
    @Column(name = "community_id", updatable = false)
    private Long communityId;

    @Column(name = "community_name",nullable = false)
    private String communityName;
    @Column(name = "community_description",nullable = false)
    private String communityDescription;
    @Column(name = "admin_approval_for_post")
    private Boolean adminApprovalForPost=false;
    @Column(name = "archived", columnDefinition = "boolean default false")
    private Boolean archived;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "creation_date",nullable = false)
    private Date creationDate=new Date();

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id",nullable = false,referencedColumnName = "user_id")
    private User owner;

    @JsonBackReference("communityMembers")
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "community_members",
            joinColumns = {@JoinColumn(name = "community_id")},
            inverseJoinColumns =  {@JoinColumn(name = "user_id")}
    )
    private Set<User> members;

    @JsonBackReference("communityAdmins")
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "community_admins",
            joinColumns = {@JoinColumn(name = "community_id")},
            inverseJoinColumns =  {@JoinColumn(name = "user_id")}
    )
    private Set<User> admins;
    @JsonBackReference("communityPosts")
    @OneToMany(mappedBy = "community",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private Set<Post> posts;
}
