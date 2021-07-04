package com.kamrul.server.repositories;

import com.kamrul.server.models.compositeKey.UserAndPostCompositeKey;
import com.kamrul.server.models.post.Post;
import com.kamrul.server.models.savedPost.SavedPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SavedPostRepository extends
        JpaRepository<SavedPost, UserAndPostCompositeKey> {

    @Query("SELECT sv.post FROM SavedPost sv WHERE sv.user.userId=:userId ORDER BY sv.creationTime DESC")
    List<Post> getSavedPostByUserId(@Param("userId")Long userId);



}
