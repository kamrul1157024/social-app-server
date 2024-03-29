package com.kamrul.server.controllers;

import com.kamrul.server.MockRequest;
import com.kamrul.server.fixtureFactories.MedalFixtureFactory;
import com.kamrul.server.fixtureFactories.PostFixtureFactory;
import com.kamrul.server.fixtureFactories.UserFixtureFactory;
import com.kamrul.server.models.medal.MedalType;
import com.kamrul.server.models.post.Post;
import com.kamrul.server.models.user.User;
import com.kamrul.server.repositories.UserRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class MedalControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserRepository userRepository;
    private MockRequest mockRequest;
    @Autowired
    private UserFixtureFactory userFixtureFactory;
    @Autowired
    private PostFixtureFactory postFixtureFactory;
    @Autowired
    private MedalFixtureFactory medalFixtureFactory;
    private Post post;
    private User user1;

    @BeforeEach
    void setUp(){
        mockRequest = new MockRequest(mockMvc,userRepository);
        user1 = userFixtureFactory.createAUser();
        post = postFixtureFactory.createAPost(user1);
    }

    @Test
    void shouldGiveMedalToPostProperly() throws Exception {
        mockRequest.put(String.format("/api/medal/post/%s/medal/%s",post.getPostId(),"GOLD"),null)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalGold").value(1))
                .andExpect(jsonPath("$.totalBronze").value(0))
                .andExpect(jsonPath("$.totalSilver").value(0));
    }

    @Test
    void shouldResetMedalsIfGivenNO_MEDAL() throws Exception{
        medalFixtureFactory.giveMedal(user1,post,MedalType.GOLD);
        mockRequest.put(String.format("/api/medal/post/%s/medal/%s",post.getPostId(),"NO_MEDAL"),null)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalGold").value(0));
    }

    @Test
    void shouldNotChangeMedalCountIfSameMedalIsGivenTwice() throws Exception{
        mockRequest.put(String.format("/api/medal/post/%s/medal/GOLD",post.getPostId()),null)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalGold").value(1));
        mockRequest.put(String.format("/api/medal/post/%s/medal/GOLD",post.getPostId()),null)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalGold").value(1));
    }
}
