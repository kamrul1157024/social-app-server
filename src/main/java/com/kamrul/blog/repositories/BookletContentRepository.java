package com.kamrul.blog.repositories;

import com.kamrul.blog.models.booklet.BookletContent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookletContentRepository extends JpaRepository<BookletContent,Long> {

}
