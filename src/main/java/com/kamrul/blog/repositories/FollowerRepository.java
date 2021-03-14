package com.kamrul.blog.repositories;

import com.kamrul.blog.models.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Set;

public interface FollowerRepository extends JpaRepository<User,Long> {

    @Query(value = "SELECT u.followed FROM User u  WHERE u.userId=:userId")
    Set<User> getAllUserCurrentUserFollows(@Param("userId") Long userId);
}
