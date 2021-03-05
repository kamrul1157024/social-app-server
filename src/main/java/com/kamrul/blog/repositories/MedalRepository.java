package com.kamrul.blog.repositories;

import com.kamrul.blog.dto.MedalDTO;
import com.kamrul.blog.models.Medal;
import com.kamrul.blog.models.MedalCompositeKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MedalRepository extends JpaRepository<Medal, MedalCompositeKey> {

    @Query(value = "SELECT new com.kamrul.blog.dto.MedalDTO(m.user.userId,m.post.postId,m.medalType) FROM  Medal m WHERE m.id=:medalCompositeKey")
    Optional<MedalDTO> findMedalByCompositeKey(@Param("medalCompositeKey") MedalCompositeKey medalCompositeKey);

}
