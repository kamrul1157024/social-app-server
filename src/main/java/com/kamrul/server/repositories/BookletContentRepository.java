package com.kamrul.server.repositories;

import com.kamrul.server.models.booklet.BookletContent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookletContentRepository extends JpaRepository<BookletContent,Long> {

}
