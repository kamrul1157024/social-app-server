package com.kamrul.blog.repositories;

import com.kamrul.blog.dto.MedalDTO;
import com.kamrul.blog.models.Medal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MedalRepository extends JpaRepository<Medal,Long> {

    @Query(value = "SELECT new com.kamrul.blog.dto.MedalDTO(m.medalId,m.medalType) FROM  Medal m where m.user.userId=:userId and m.post.postId=:postId")
    Optional<MedalDTO> findMedalByUserIdAndPostID(@Param("userId") Long userId, @Param("postId") Long postId);

}
