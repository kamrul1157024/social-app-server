package com.kamrul.blog.repositories;

import com.kamrul.blog.dto.MedalDTO;
import com.kamrul.blog.models.post.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostRepository extends JpaRepository<Post,Long> {


    @Query(value = "SELECT p FROM Post p LEFT JOIN p.tags ORDER BY (p.totalGold * 3L + p.totalSilver*2L + p.totalBronze) DESC ")
    Page<Post> getTopPost(Pageable pageable);

    @Query(value = "SELECT p FROM Post p WHERE p.user.userId=:userId ORDER BY (p.totalGold * 3L + p.totalSilver*2L + p.totalBronze) DESC")
    Page<Post> getPostByUserId(@Param("userId") Long userId,Pageable pageable);


}
