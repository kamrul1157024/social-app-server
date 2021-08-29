package com.kamrul.server.repositories;

import com.kamrul.server.models.post.Post;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

@SpringBootTest
class TagRepositoryTest {

    @Autowired
    TagRepository tagRepository;

    @Test
    void getPostTagByPostTagNames() {
        String tagName="History";
        List<Post> posts= tagRepository.getPostTagByPostTagName(tagName);
        posts.forEach(post -> System.out.println(post.getPostTitle()));
    }

    @Test
    void getTagByName() {
    }

    @Test
    void testGetPostTagByPostTagNames() {
        String[] tagNames=new String[]{"History","Literature"};
        List<Post> posts=tagRepository.getPostTagByPostTagNames(Arrays.asList(tagNames));
        posts.forEach(post -> System.out.println(post.getPostId()));
    }
}