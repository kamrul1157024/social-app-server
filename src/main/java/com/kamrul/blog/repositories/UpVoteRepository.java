package com.kamrul.blog.repositories;

import com.kamrul.blog.models.UpVote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UpVoteRepository extends JpaRepository<UpVote,Long> {

    @Query(value = "SELECT u FROM  UpVote u where u.user.userId=:userId and u.post.postId=:postId")
    Optional<UpVote> findUpVoteByUserIdAndPostID(@Param("userId") Long userId,@Param("postId") Long postId);

}
