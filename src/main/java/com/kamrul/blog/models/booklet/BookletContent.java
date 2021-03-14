package com.kamrul.blog.models.booklet;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "booklet_content")
public class BookletContent {

    @Id
    @SequenceGenerator(
            name = "booklet_content_id_generator",
            sequenceName = "booklet_content_id_generator",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "booklet_content_id_generator"
    )
    @Column(name = "booklet_content_id", updatable = false)
    private Long bookletContentId;
    @Column(name = "serial_no",unique = false,nullable = false)
    private Integer serialNo;
    @Column(name = "title", nullable = false,columnDefinition = "TEXT")
    private String title;
    @Column(name = "details",nullable = false,columnDefinition = "TEXT")
    private String details;
    @Column(name = "link",nullable = false,columnDefinition = "TEXT")
    private String link;
    @Column(name = "is_external_link")
    private Boolean isExternalLink;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "booklet_id",referencedColumnName = "booklet_id")
    private Booklet booklet;

    public BookletContent(
            Integer serialNo,
            String title,
            String details,
            String link,
            Boolean isExternalLink,
            Booklet booklet) {
        this.serialNo = serialNo;
        this.title = title;
        this.details = details;
        this.link = link;
        this.isExternalLink = isExternalLink;
        this.booklet = booklet;
    }

    public BookletContent() {
    }

    public Long getBookletContentId() {
        return bookletContentId;
    }

    public void setBookletContentId(Long bookletContentId) {
        this.bookletContentId = bookletContentId;
    }

    public Integer getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(Integer serialNo) {
        this.serialNo = serialNo;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public Boolean getExternalLink() {
        return isExternalLink;
    }

    public void setExternalLink(Boolean externalLink) {
        isExternalLink = externalLink;
    }

    @JsonBackReference("booklet")
    public Booklet getBooklet() {
        return booklet;
    }

    public void setBooklet(Booklet booklet) {
        this.booklet = booklet;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookletContent that = (BookletContent) o;
        return getBookletContentId().equals(that.getBookletContentId()) && getSerialNo().equals(that.getSerialNo()) && getTitle().equals(that.getTitle()) && getDetails().equals(that.getDetails()) && getLink().equals(that.getLink()) && isExternalLink.equals(that.isExternalLink) && Objects.equals(getBooklet(), that.getBooklet());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getBookletContentId(), getSerialNo(), getTitle(), getDetails(), getLink(), isExternalLink, getBooklet());
    }

    @Override
    public String toString() {
        return "BookletContent{" +
                "bookletContentId=" + bookletContentId +
                ", serialNo=" + serialNo +
                ", title='" + title + '\'' +
                ", details='" + details + '\'' +
                ", link='" + link + '\'' +
                ", isExternalLink=" + isExternalLink +
                ", booklet=" + booklet +
                '}';
    }
}
