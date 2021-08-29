package com.kamrul.server.repositories;

import com.kamrul.server.dto.PostDTO;
import com.kamrul.server.models.post.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PostRepository extends JpaRepository<Post,Long> {


    @Query(value = "SELECT p FROM Post p LEFT JOIN p.tags ORDER BY (p.totalGold * 3L + p.totalSilver*2L + p.totalBronze) DESC ,p.creationDate ASC")
    Page<Post> getTopPost(Pageable pageable);

    @Query(value = "SELECT p FROM Post p WHERE p.user.userId=:userId AND p.isDraft=false ORDER BY p.creationDate DESC ")
    Page<Post> getPostByUserId(@Param("userId") Long userId,Pageable pageable);

    @Query(value = "SELECT new com.kamrul.server.dto.PostDTO(p,m.medalType)" +
            " FROM Post p LEFT JOIN Medal m on (m.post.postId=p.postId AND m.user.userId=:loggedInUserId)" +
            "WHERE p.user.userId=:userId AND p.isDraft=false ORDER BY p.creationDate DESC ")
    Page<PostDTO> getPostsByUserIdWithMedalsGivenByLoggedInUser(
            @Param("userId")Long userId,
            @Param("loggedInUserId") Long loggedInUserId,
            Pageable pageable
    );
}
