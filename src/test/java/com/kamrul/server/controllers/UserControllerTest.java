package com.kamrul.server.controllers;

import com.github.javafaker.Faker;
import com.kamrul.server.MockRequest;
import com.kamrul.server.dto.UserDTO;
import com.kamrul.server.fixtures.MedalFixture;
import com.kamrul.server.fixtures.PostFixture;
import com.kamrul.server.fixtures.UserFixture;
import com.kamrul.server.models.medal.MedalType;
import com.kamrul.server.models.post.Post;
import com.kamrul.server.models.user.User;
import com.kamrul.server.repositories.UserRepository;
import com.kamrul.server.utils.Converters;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserControllerTest {
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

    @BeforeAll
    void setUp(){
        mockRequest = new MockRequest(mockMvc,userRepository);
    }

    @Test
    void shouldGetUserByUserId() throws Exception {
        User user = userFixture.createAUser();
        ExpectationMatcher.expectToBeUser(
                mockRequest.get(String.format("/api/user/%s",user.getUserId())),
                        user
        );
    }

    @Test
    void shouldGetUserUsingUserNameAnShouldNotShowEmail() throws Exception{
        User override = new User();
        override.setEmailVisible(false);
        User user = userFixture.createAUser(override);
        ExpectationMatcher.expectToBeUser(
                        mockRequest.get(String.format("/api/user/userName/%s", user.getUserName())),
                        user
        );
    }

    @Test
    void shouldGetUserUsingUserNameAnShouldShowEmail() throws Exception{
        User override = new User();
        override.setEmailVisible(true);
        User user = userFixture.createAUser(override);
        ExpectationMatcher.expectToBeUser(
                        mockRequest.get(String.format("/api/user/userName/%s", user.getUserName())),
                        user
        );
    }

    @Test
    void shouldGetCurrentlyLoggedInUser() throws Exception{
        ExpectationMatcher.expectToBeUser(
                        mockRequest.get("/api/user/currentlyLoggedInUser"),
                        mockRequest.getUser()
        );
    }

    @Test
    void shouldUpdateUserProperly() throws Exception{
        Faker faker = new Faker();
        User user = mockRequest.getUser();
        UserDTO userDTO = Converters.convert(user);
        String updatedEmail = String.format("%s@gamil.com",faker.name().username());
        userDTO.setEmail(updatedEmail);
        userDTO.setEmailVisible(true);
        user.setEmail(updatedEmail);
        user.setEmailVisible(true);
        ExpectationMatcher.expectToBeUser(
            mockRequest.put("/api/user",userDTO),
                user
        );
    }

    @Test
    void shouldGetUserPostWithMedalProvidedByCurrentUser() throws Exception {
        User user = userFixture.createAUser();
        Post[] posts = new Post[5];
        for (int i = 0; i < 3; i++) {
            posts[i] = postFixture.createAPost(user);
        }
        User currentUser = mockRequest.getUser();
        medalFixture.giveMedal(currentUser,posts[0],MedalType.GOLD);
        medalFixture.giveMedal(currentUser,posts[2], MedalType.BRONZE);
        mockRequest.get(String.format("/api/user/%s/posts?pageNo=1",user.getUserId()))
                .andExpect(jsonPath("$[0].postId").value(posts[2].getPostId()))
                .andExpect(jsonPath("$[0].medalTypeProvidedByLoggedInUser").value(MedalType.BRONZE.toString()))
                .andExpect(jsonPath("$[0].user.userId").value(posts[2].getUser().getUserId()))
                .andExpect(jsonPath("$[1].postId").value(posts[1].getPostId()))
                .andExpect(jsonPath("$[1].medalTypeProvidedByLoggedInUser").value(MedalType.NO_MEDAL.toString()))
                .andExpect(jsonPath("$[1].user.userId").value(posts[1].getUser().getUserId()))
                .andExpect(jsonPath("$[2].postId").value(posts[0].getPostId()))
                .andExpect(jsonPath("$[2].medalTypeProvidedByLoggedInUser").value(MedalType.GOLD.toString()))
                .andExpect(jsonPath("$[2].user.userId").value(posts[0].getUser().getUserId()));
    }

    @Test
    void shouldDeleteLoggedInUser() throws Exception{
        mockRequest.delete("/api/user").andExpect(status().isOk());
    }
}
