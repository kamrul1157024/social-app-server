package com.kamrul.server.controllers;

import com.github.javafaker.Faker;
import com.kamrul.server.MockRequest;
import com.kamrul.server.dto.PostDTO;
import com.kamrul.server.services.verify.PostVerifier;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
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

    @Test
    @Order(1)
    void shouldCreatePost() throws Exception{
        PostDTO postDTO = new PostDTO();
        postDTO.setPostText(faker.lorem().paragraph(1));
        postDTO.setPostTitle(faker.lorem().sentence(2));
        postDTO.setDraft(false);
        doNothing().when(postVerifier).verify(postDTO);
        mockRequest.post("/api/post",postDTO)
                .andExpect(jsonPath("$.postTitle").isString())
                .andExpect(jsonPath("$.postText").isString())
                .andExpect(jsonPath("$.draft").value(false))
                .andExpect(jsonPath("$.user").exists());
    }
}