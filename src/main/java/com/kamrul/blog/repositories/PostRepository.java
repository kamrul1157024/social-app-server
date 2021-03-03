package com.kamrul.blog.repositories;

import com.kamrul.blog.dto.MedalDTO;
import com.kamrul.blog.dto.PostDTO;
import com.kamrul.blog.models.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostRepository extends JpaRepository<Post,Long> {


    @Query(value = "SELECT p FROM Post p LEFT JOIN p.tags ORDER BY (p.totalGold * 3L + p.totalSilver*2L + p.totalBronze) DESC ")
    Page<Post> getTopPost(Pageable pageable);

    @Query(value = "SELECT new com.kamrul.blog.dto.MedalDTO(m.post.postId,m.medalType) FROM Medal m JOIN m.post WHERE m.user.userId=:loggedInUserId")
    List<MedalDTO> getPostForCurrentlyLoggedInUserOnWhichUserGivenMedal(
            @Param("loggedInUserId") Long loggedInUserId
    );

    @Query(value = "SELECT new com.kamrul.blog.dto.MedalDTO(m.post.postId,m.medalType) FROM Medal m WHERE m.user.userId=:loggedInUserId and m.post.postId In (SELECT m.post.postId FROM Post p WHERE (p.user.userId=:userId))")
    List<MedalDTO> getPostIdForUserIdOnWhichCurrentlyLoggedInUserGivenMedal(
            @Param("loggedInUserId") Long loggedInUserId,
            @Param("userId") Long userId
    );

    @Query(value = "SELECT p FROM Post p WHERE p.user.userId=:userId ORDER BY (p.totalGold * 3L + p.totalSilver*2L + p.totalBronze) DESC")
    Page<Post> getPostByUserId(@Param("userId") Long userId,Pageable pageable);


}
