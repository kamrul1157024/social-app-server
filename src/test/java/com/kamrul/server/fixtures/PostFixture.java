package com.kamrul.server.fixtures;

import com.github.javafaker.Faker;
import com.kamrul.server.Utils;
import com.kamrul.server.models.compositeKey.UserAndPostCompositeKey;
import com.kamrul.server.models.medal.Medal;
import com.kamrul.server.models.medal.MedalType;
import com.kamrul.server.models.post.Post;
import com.kamrul.server.models.user.User;
import com.kamrul.server.repositories.MedalRepository;
import com.kamrul.server.repositories.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PostFixture {
    private final PostRepository postRepository;

    @Autowired
    public PostFixture(PostRepository postRepository){
        this.postRepository = postRepository;
    }

    public Post createAPost(User user, Post ...overrides) {
        Faker faker = new Faker();
        Post post = new Post();
        post.setPostTitle(faker.lorem().sentence(10));
        post.setPostText(faker.lorem().paragraph(10));
        post.setDraft(false);
        post.setUser(user);
        post = Utils.override(post,overrides);
        return postRepository.save(post);
    }
}
