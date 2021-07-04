package com.kamrul.server.repositories;

import com.kamrul.server.models.post.Post;
import com.kamrul.server.models.tag.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TagRepository extends JpaRepository<Tag,Long> {

    @Query("SELECT DISTINCT pt.posts FROM Tag pt WHERE pt.tagName IN :tagNames")
    List<Post> getPostTagByPostTagNames(@Param("tagNames") List<String> postTagNames);

    @Query("SELECT DISTINCT pt.posts FROM Tag pt WHERE pt.tagName = :tagName")
    List<Post> getPostTagByPostTagName(@Param("tagName") String postTagName);

    @Query("SELECT t FROM Tag t WHERE t.tagName=:tagName")
    Tag getTagByName(@Param("tagName") String tagName);

    @Query("SELECT t FROM Tag t")
    List<Tag> getAllTags();
}
