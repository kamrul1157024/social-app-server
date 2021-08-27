package com.kamrul.server.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import com.kamrul.server.MockRequest;
import com.kamrul.server.dto.PostDTO;
import com.kamrul.server.models.post.Post;
import com.kamrul.server.services.verify.PostVerifier;
import com.kamrul.server.services.verify.exception.VerificationException;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.tomcat.util.json.JSONParser;
import org.json.JSONObject;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import javax.servlet.http.HttpServletResponse;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("controller_test")
@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PostControllerTest  {

    @Autowired
    private MockMvc mockMvc;
    private MockRequest mockRequest;
    private Faker faker = new Faker();
    @Autowired
    PostVerifier postVerifier;

    @BeforeAll
    void setUp() throws Exception{
        mockRequest =  new MockRequest(mockMvc);
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
        mockRequest.get(String.format("/api/post/%s",post.getPostId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.postId").isNotEmpty())
                .andExpect(jsonPath("$.postTitle").value(postDTO.getPostTitle()))
                .andExpect(jsonPath("$.postText").value(postDTO.getPostText()))
                .andExpect(jsonPath("$.draft").value(false))
                .andExpect(jsonPath("$.user").isNotEmpty());
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
    void shouldDeletePostProperly() throws Exception{
        ImmutablePair<Post,PostDTO> pair = createPost();
        Post post = pair.getLeft();
        mockRequest.delete(String.format("/api/post/%s",post.getPostId()),post)
                .andExpect(status().isOk());
    }
}
