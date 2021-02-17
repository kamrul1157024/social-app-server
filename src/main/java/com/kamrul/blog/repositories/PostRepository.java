package com.kamrul.blog.repositories;

import com.kamrul.blog.models.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PostRepository extends JpaRepository<Post,Long> {

    @Query(value = "SELECT p FROM Post p WHERE p.postId<30")
    List<Post> getTopPost();

}
