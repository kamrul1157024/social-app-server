package com.kamrul.blog.repositories;

import com.kamrul.blog.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FollowerRepository extends JpaRepository<User,Long> {
}
