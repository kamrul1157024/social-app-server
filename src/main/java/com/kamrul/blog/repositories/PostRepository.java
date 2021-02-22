package com.kamrul.blog.repositories;

import com.kamrul.blog.models.Post;
import com.kamrul.blog.models.User;
import org.hibernate.annotations.Fetch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostRepository extends JpaRepository<Post,Long> {


    @Query(value = "SELECT p FROM Post p ORDER BY p.totalUpVotes DESC ")
    Page<Post> getTopPost(Pageable pageable);

    @Query(value = "SELECT uv.post.postId FROM UpVote uv JOIN uv.post p WHERE uv.user.userId=:loggedInUserId")
    List<Long> getPostWhichIsUpVotedByCurrentlyLoggedInUser(@Param("loggedInUserId") Long loggedInUserId);

    @Query(
            value = "SELECT uv.post.postId " +
                    "FROM UpVote uv" +
                    " WHERE uv.post.postId In (SELECT p.postId FROM Post p WHERE (p.user.userId=:userId)) " +
                    "and uv.user.userId=:loggedInUserId ")

    List<Long> getPostIdForUserIdWhichIsUpVotedByCurrentlyLoggedInUser(
            @Param("loggedInUserId") Long loggedInUserId,
            @Param("userId") Long userId
    );


}
