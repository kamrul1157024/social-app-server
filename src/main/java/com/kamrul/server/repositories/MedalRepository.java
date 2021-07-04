package com.kamrul.server.repositories;

import com.kamrul.server.dto.MedalDTO;
import com.kamrul.server.models.compositeKey.UserAndPostCompositeKey;
import com.kamrul.server.models.medal.Medal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MedalRepository extends JpaRepository<Medal, UserAndPostCompositeKey> {

    @Query(value = "SELECT new com.kamrul.server.dto.MedalDTO(m.user.userId,m.post.postId,m.medalType) FROM  Medal m WHERE m.userAndPostCompositeKey=:userAndPostCompositeKey")
    Optional<MedalDTO> findMedalByCompositeKey(@Param("userAndPostCompositeKey") UserAndPostCompositeKey userAndPostCompositeKey);

    @Query(value = "SELECT new com.kamrul.server.dto.MedalDTO(m.user.userId,m.post.postId,m.medalType) FROM Medal m JOIN m.post WHERE m.user.userId=:loggedInUserId")
    List<MedalDTO> getPostForCurrentlyLoggedInUserOnWhichUserGivenMedal(
            @Param("loggedInUserId") Long loggedInUserId
    );

    @Query(value = "SELECT new com.kamrul.server.dto.MedalDTO(m.user.userId,m.post.postId,m.medalType) FROM Medal m WHERE m.user.userId=:loggedInUserId and m.post.postId In (SELECT m.post.postId FROM Post p WHERE (p.user.userId=:userId))")
    List<MedalDTO> getPostIdForUserIdOnWhichCurrentlyLoggedInUserGivenMedal(
            @Param("loggedInUserId") Long loggedInUserId,
            @Param("userId") Long userId
    );

}
