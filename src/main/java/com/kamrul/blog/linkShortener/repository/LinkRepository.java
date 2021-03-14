package com.kamrul.blog.linkShortener.repository;

import com.kamrul.blog.linkShortener.model.Link;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LinkRepository extends JpaRepository<Link,Long> {

}
