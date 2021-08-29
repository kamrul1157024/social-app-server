package com.kamrul.server.repositories;

import com.kamrul.server.models.compositeKey.UserAndPostCompositeKey;
import com.kamrul.server.models.medal.Medal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MedalRepository extends JpaRepository<Medal, UserAndPostCompositeKey> {

    @Query("SELECT m From Medal m WHERE m.userAndPostCompositeKey=:userAndPostCompositeKey")
    Optional<Medal> findMedalByUserAndPostCompositeKey(@Param("userAndPostCompositeKey") UserAndPostCompositeKey userAndPostCompositeKey);

    @Query(value = "SELECT m FROM Medal m WHERE m.user.userId=:userId")
    List<Medal> findMedalsByUserId(@Param("userId") Long userId);

    @Query(value = "SELECT m FROM Medal m WHERE m.user.userId=:loggedInUserId and m.post.postId In (SELECT p.postId FROM Post p WHERE (p.user.userId=:userId))")
    List<Medal> findMedalsThatCurrentlyLoggedInUserGivenToUserId(
            @Param("loggedInUserId") Long loggedInUserId,
            @Param("userId") Long userId
    );

    @Query(value = "SELECT m From Medal m WHERE m.userAndPostCompositeKey.postId=:postId")
    List<Medal> findMedalsByPostId(@Param("postId") Long postId);

}
