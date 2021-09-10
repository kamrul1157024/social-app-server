package com.kamrul.server.repositories;

import com.kamrul.server.dto.PostDTO;
import com.kamrul.server.fixtureFactories.MedalFixtureFactory;
import com.kamrul.server.fixtureFactories.PostFixtureFactory;
import com.kamrul.server.fixtureFactories.UserFixtureFactory;
import com.kamrul.server.models.medal.MedalType;
import com.kamrul.server.models.post.Post;
import com.kamrul.server.models.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PostRepositoryTest {

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserFixtureFactory userFixtureFactory;
    @Autowired
    private PostFixtureFactory postFixtureFactory;
    @Autowired
    private MedalFixtureFactory medalFixtureFactory;
    private User user1,user2;
    private Post[] posts = new Post[3];

    @BeforeEach
    void setUp(){
        user1 = userFixtureFactory.createAUser();
        user2 = userFixtureFactory.createAUser();
            for (int j = 0; j < 3; j++) {
                posts[j] = postFixtureFactory.createAPost(user1);
            }
    }

    @Test
    void shouldGetPostsByUserIdWithMedalsGivenByLoggedInUser(){
        medalFixtureFactory.giveMedal(user2,posts[0], MedalType.GOLD);
        medalFixtureFactory.giveMedal(user2,posts[1], MedalType.SILVER);
        Page<PostDTO> postDTOPage = postRepository.
                getPostsByUserIdWithMedalsGivenByLoggedInUser(
                        user1.getUserId(),
                        user2.getUserId(),
                        PageRequest.of(0,10)
                );
        List<PostDTO> postDTOList = postDTOPage.toList();
        for (int i = 0; i < 3; i++) {
            assertEquals(posts[i].getPostId(),postDTOList.get(2-i).getPostId());
        }
        assertEquals(MedalType.GOLD,postDTOList.get(2).medalTypeProvidedByLoggedInUser);
        assertEquals(MedalType.SILVER,postDTOList.get(1).medalTypeProvidedByLoggedInUser);
        assertEquals(MedalType.NO_MEDAL,postDTOList.get(0).medalTypeProvidedByLoggedInUser);
    }
}
