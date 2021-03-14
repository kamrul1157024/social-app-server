package com.kamrul.blog.linkShortener.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "link_shortener")
public class Link {

    @Id
    @Column(name = "hashedLink",nullable = false,unique = true,updatable = false)
    private Long hashedLink;
    @Column(name = "link",nullable = false)
    private String link;

    public Long getHashedLink() {
        return hashedLink;
    }

    public void setHashedLink(Long hashedLink) {
        this.hashedLink = hashedLink;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    @Override
    public String toString() {
        return "Link{" +
                "hashedLink='" + hashedLink + '\'' +
                ", link='" + link + '\'' +
                '}';
    }
}
