package com.kamrul.blog.repositories;

import com.kamrul.blog.models.booklet.Booklet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookletRepository extends JpaRepository<Booklet,Long> {

    @Query("SELECT b FROM Booklet b WHERE b.user.userId=:userId")
    List<Booklet> getBookletByUserId(@Param("userId") Long userId);
}
