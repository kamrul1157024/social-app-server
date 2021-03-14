package com.kamrul.blog.models.booklet;

import com.kamrul.blog.models.user.User;

import javax.persistence.*;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "booklet")
public class Booklet {

    @Id
    @SequenceGenerator(
            name = "booklet_id_generator",
            sequenceName = "booklet_id_generator",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "booklet_id_generator"
    )
    @Column(name = "booklet_id", updatable = false)
    private Long bookletId;
    @Column(name = "booklet_title", nullable = false, columnDefinition = "TEXT")
    private String bookletTitle;
    @Column(name = "booklet_description", nullable = false, columnDefinition = "TEXT")
    private String bookletDescription;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "creation_date", nullable = false)
    private Date creationDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "booklet", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    List<BookletContent> bookletContents;


    private void init() {
        this.creationDate = new Date();
    }

    public Booklet(String bookletTitle,
                   String bookletDescription,
                   Collection<BookletContent> bookletContents) {

        this.bookletTitle = bookletTitle;
        this.bookletDescription = bookletDescription;
        this.bookletContents = (List<BookletContent>) bookletContents;
    }

    public Booklet() {
        init();
    }

    public Long getBookletId() {
        return bookletId;
    }

    public void setBookletId(Long bookletId) {
        this.bookletId = bookletId;
    }

    public String getBookletTitle() {
        return bookletTitle;
    }

    public void setBookletTitle(String bookletTitle) {
        this.bookletTitle = bookletTitle;
    }

    public String getBookletDescription() {
        return bookletDescription;
    }

    public void setBookletDescription(String bookletDescription) {
        this.bookletDescription = bookletDescription;
    }

    public List<BookletContent> getBookletContents() {
        return bookletContents;
    }

    public void setBookletContents(Collection<BookletContent> bookletContents) {
        this.bookletContents = (List<BookletContent>) bookletContents;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Booklet booklet = (Booklet) o;
        return getBookletId().equals(booklet.getBookletId()) && getBookletTitle().equals(booklet.getBookletTitle()) && getBookletDescription().equals(booklet.getBookletDescription()) && getCreationDate().equals(booklet.getCreationDate()) && getUser().equals(booklet.getUser()) && Objects.equals(getBookletContents(), booklet.getBookletContents());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getBookletId(), getBookletTitle(), getBookletDescription(), getCreationDate(), getUser(), getBookletContents());
    }

    @Override
    public String toString() {
        return "Booklet{" +
                "bookletId=" + bookletId +
                ", bookletTitle='" + bookletTitle + '\'' +
                ", bookletDescription='" + bookletDescription + '\'' +
                ", creationDate=" + creationDate +
                ", user=" + user +
                ", bookletContents=" + bookletContents +
                '}';
    }
}
