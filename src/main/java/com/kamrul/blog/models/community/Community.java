package com.kamrul.blog.models.community;

import com.kamrul.blog.models.user.User;
import lombok.*;

import javax.persistence.*;
import java.util.Date;

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

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "creation_date",nullable = false)
    private Date creationDate=new Date();

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id",nullable = false,referencedColumnName = "user_id")
    private User owner;

}
