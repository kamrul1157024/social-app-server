package com.kamrul.server.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import com.kamrul.server.MockRequest;
import com.kamrul.server.dto.PostDTO;
import com.kamrul.server.fixtureFactories.MedalFixtureFactory;
import com.kamrul.server.fixtureFactories.PostFixtureFactory;
import com.kamrul.server.fixtureFactories.UserFixtureFactory;
import com.kamrul.server.models.medal.MedalType;
import com.kamrul.server.models.post.Post;
import com.kamrul.server.models.user.User;
import com.kamrul.server.repositories.UserRepository;
import com.kamrul.server.services.verify.PostVerifier;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("controller_test")
@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PostControllerTest  {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserRepository userRepository;
    private MockRequest mockRequest;
    @Autowired
    private PostFixtureFactory postFixtureFactory;
    @Autowired
    private UserFixtureFactory userFixtureFactory;
    @Autowired
    private MedalFixtureFactory medalFixtureFactory;
    private final Faker faker = new Faker();
    @Autowired
    private PostVerifier postVerifier;

    @BeforeAll
    void setUp(){
        mockRequest = new MockRequest(mockMvc,userRepository);
    }

    ImmutablePair<Post,PostDTO> createPost() throws Exception {
        PostDTO postDTO = new PostDTO();
        postDTO.setPostText(faker.lorem().paragraph(1));
        postDTO.setPostTitle(faker.lorem().sentence(2));
        postDTO.setDraft(false);
        doNothing().when(postVerifier).verify(postDTO);
        ObjectMapper objectMapper = new ObjectMapper();
        return new ImmutablePair<>(
                objectMapper.readValue(
                        mockRequest.post("/api/post",postDTO)
                                .andReturn()
                                .getResponse()
                                .getContentAsString(), Post.class),
                postDTO
        );
    }

    @Test
    void shouldCreatePost() throws Exception{
        PostDTO postDTO = new PostDTO();
        postDTO.setPostText(faker.lorem().paragraph(1));
        postDTO.setPostTitle(faker.lorem().sentence(2));
        postDTO.setDraft(false);
        doNothing().when(postVerifier).verify(postDTO);
        mockRequest.post("/api/post",postDTO)
                .andExpect(jsonPath("$.postId").isNotEmpty())
                .andExpect(jsonPath("$.postTitle").value(postDTO.getPostTitle()))
                .andExpect(jsonPath("$.postText").value(postDTO.getPostText()))
                .andExpect(jsonPath("$.draft").value(false))
                .andExpect(jsonPath("$.user").exists());
    }

    @Test
    void shouldGetPostByPostId() throws Exception{
        ImmutablePair<Post,PostDTO> pair = createPost();
        Post post = pair.getLeft();
        PostDTO postDTO = pair.getRight();
        medalFixtureFactory.giveMedal(mockRequest.getUser(),post,MedalType.BRONZE);
        mockRequest.get(String.format("/api/post/%s",post.getPostId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.postId").isNotEmpty())
                .andExpect(jsonPath("$.postTitle").value(postDTO.getPostTitle()))
                .andExpect(jsonPath("$.postText").value(postDTO.getPostText()))
                .andExpect(jsonPath("$.draft").value(false))
                .andExpect(jsonPath("$.medalTypeProvidedByLoggedInUser").value(MedalType.BRONZE.toString()))
                .andExpect(jsonPath("$.user").isNotEmpty());
    }

    @Test
    void shouldNotGetDraftPostOfDifferentUser() throws Exception{
        User user = userFixtureFactory.createAUser();
        Post overrides  = new Post();
        overrides.setDraft(true);
        Post post = postFixtureFactory.createAPost(user,overrides);
        mockRequest.get(String.format("/api/post/%s",post.getPostId()))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldNotGetDraftPostByUnAuthorizedUser() throws Exception{
        User user = userFixtureFactory.createAUser();
        Post overrides = new Post();
        overrides.setDraft(true);
        Post post = postFixtureFactory.createAPost(user,overrides);
        mockRequest.getAsUnAuthorized(String.format("/api/post/%s",post.getPostId()))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldUpdatePostProperly() throws Exception{
        ImmutablePair<Post,PostDTO> pair = createPost();
        Post post = pair.getLeft();
        PostDTO postDTO =  pair.getRight();
        mockRequest.put("/api/post/",post)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.postId").value(post.getPostId()))
                .andExpect(jsonPath("$.postTitle").value(postDTO.getPostTitle()))
                .andExpect(jsonPath("$.postText").value(postDTO.getPostText()))
                .andExpect(jsonPath("$.draft").value(false))
                .andExpect(jsonPath("$.user").isNotEmpty());
    }

    @Test
    void shouldReturnMedalGiverOfThePostProperly() throws Exception{
        User user1 = userFixtureFactory.createAUser();
        User user2 = userFixtureFactory.createAUser();
        User user3 = userFixtureFactory.createAUser();
        Post post = postFixtureFactory.createAPost(user1);
        medalFixtureFactory.giveMedal(user1,post, MedalType.SILVER);
        medalFixtureFactory.giveMedal(user2,post, MedalType.GOLD);
        medalFixtureFactory.giveMedal(user3,post,MedalType.SILVER);
        mockRequest.get(String.format("/api/post/%s/medalGivers",post.getPostId()))
                .andExpect(jsonPath("$[0].user.userId").value(user1.getUserId()))
                .andExpect(jsonPath("$[1].user.userId").value(user2.getUserId()))
                .andExpect(jsonPath("$[2].user.userId").value(user3.getUserId()));
    }

    @Test
    void shouldDeletePostProperly() throws Exception{
        User overrides = new User();
        overrides.setLastName("kamrul");
        overrides.setIsEmailVerified(true);
        userFixtureFactory.createAUser(overrides);
        ImmutablePair<Post,PostDTO> pair = createPost();
        Post post = pair.getLeft();
        mockRequest.delete(String.format("/api/post/%s",post.getPostId()),post)
                .andExpect(status().isOk());
    }
}
