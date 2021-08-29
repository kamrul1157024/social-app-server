package com.kamrul.server.controllers;

import com.kamrul.server.MockRequest;
import com.kamrul.server.dto.MedalDTO;
import com.kamrul.server.fixtures.MedalFixture;
import com.kamrul.server.fixtures.PostFixture;
import com.kamrul.server.fixtures.UserFixture;
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
    private UserFixture userFixture;
    @Autowired
    private PostFixture postFixture;
    @Autowired
    private MedalFixture medalFixture;
    private Post post;
    private User user1,user2;

    @BeforeEach
    void setUp(){
        mockRequest = new MockRequest(mockMvc,userRepository);
        user1 = userFixture.createAUser();
        user2 = userFixture.createAUser();
        post = postFixture.createAPost(user1);
    }

    @Test
    void shouldGiveMedalToPostProperly() throws Exception {
        MedalDTO medalDTO = new MedalDTO();
        medalDTO.setMedalType(MedalType.GOLD);
        mockRequest.put(String.format("/api/medal/post/%s",post.getPostId()),medalDTO)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalGold").value(1))
                .andExpect(jsonPath("$.totalBronze").value(0))
                .andExpect(jsonPath("$.totalSilver").value(0));
    }

    @Test
    void shouldResetMedalsIfGivenNO_MEDAL() throws Exception{
        medalFixture.giveMedal(user1,post,MedalType.GOLD);
        MedalDTO medalDTO = new MedalDTO();
        medalDTO.setMedalType(MedalType.NO_MEDAL);
        mockRequest.put(String.format("/api/medal/post/%s",post.getPostId()),medalDTO)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalGold").value(0));
    }

    @Test
    void shouldNotChangeMedalCountIfSameMedalIsGivenTwice() throws Exception{
        MedalDTO medalDTO = new MedalDTO();
        medalDTO.setMedalType(MedalType.GOLD);
        mockRequest.put(String.format("/api/medal/post/%s",post.getPostId()),medalDTO)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalGold").value(1));
        mockRequest.put(String.format("/api/medal/post/%s",post.getPostId()),medalDTO)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalGold").value(1));
    }
}
