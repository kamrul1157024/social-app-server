package com.kamrul.server.linkShortener.repository;

import com.kamrul.server.linkShortener.model.Link;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LinkRepository extends JpaRepository<Link,Long> {

}
