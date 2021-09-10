package com.kamrul.server.fixtureFactories;

import com.github.javafaker.Faker;
import com.kamrul.server.Utils;
import com.kamrul.server.models.post.Post;
import com.kamrul.server.models.user.User;
import com.kamrul.server.repositories.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PostFixtureFactory {
    private final PostRepository postRepository;

    @Autowired
    public PostFixtureFactory(PostRepository postRepository){
        this.postRepository = postRepository;
    }

    public Post createAPost(User user, Post ...overrides) {
        Faker faker = new Faker();
        Post post = new Post();
        post.setPostTitle(faker.lorem().sentence(10));
        post.setPostText(faker.lorem().paragraph(10));
        post.setUser(user);
        post = Utils.override(post,overrides);
        if (overrides.length>0 && overrides[0].getDraft()==true)
            post.setDraft(true);
        else
            post.setDraft(false);

        return postRepository.save(post);
    }
}
