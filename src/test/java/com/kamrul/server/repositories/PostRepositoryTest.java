package com.kamrul.server.repositories;

import com.kamrul.server.dto.PostDTO;
import com.kamrul.server.fixtures.MedalFixture;
import com.kamrul.server.fixtures.PostFixture;
import com.kamrul.server.fixtures.UserFixture;
import com.kamrul.server.models.medal.MedalType;
import com.kamrul.server.models.post.Post;
import com.kamrul.server.models.user.User;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PostRepositoryTest {

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserFixture userFixture;
    @Autowired
    private PostFixture postFixture;
    @Autowired
    private MedalFixture medalFixture;
    private User user1,user2;
    private Post[] posts = new Post[3];

    @BeforeEach
    void setUp(){
        user1 = userFixture.createAUser();
        user2 = userFixture.createAUser();
            for (int j = 0; j < 3; j++) {
                posts[j] = postFixture.createAPost(user1);
            }
    }

    @Test
    void shouldGetPostsByUserIdWithMedalsGivenByLoggedInUser(){
        medalFixture.giveMedal(user2,posts[0], MedalType.GOLD);
        medalFixture.giveMedal(user2,posts[1], MedalType.SILVER);
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
