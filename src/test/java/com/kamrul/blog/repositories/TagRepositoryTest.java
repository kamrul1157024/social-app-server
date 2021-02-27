package com.kamrul.blog.repositories;

import com.kamrul.blog.models.Post;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
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