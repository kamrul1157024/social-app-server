package com.kamrul.blog.repositories;

import com.kamrul.blog.models.Post;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class PostRepositoryTest {


    @Autowired
    PostRepository postRepository;

    @Test
    void getPostIdForUserIdWhichIsUpVotedByCurrentlyLoggedInUser() {

        List<Long> postIDs=postRepository
                .getPostIdForUserIdWhichIsUpVotedByCurrentlyLoggedInUser(
                        14L,
                        14L
                );
        System.out.println(postIDs);

    }

    @Test
    void getTopPost() {
        Page<Post> posts=postRepository.getTopPost(PageRequest.of(0,10));

        posts.forEach(post-> System.out.println(post.getPostTitle()));
    }

    @Test
    void testGetPostIdForUserIdWhichIsUpVotedByCurrentlyLoggedInUser() {

        List<Long> postIds=postRepository.getPostWhichIsUpVotedByCurrentlyLoggedInUser(13L);
        postIds.forEach(postId -> System.out.println(postId));
    }
}