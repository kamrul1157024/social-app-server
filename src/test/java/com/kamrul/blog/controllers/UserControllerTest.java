package com.kamrul.blog.controllers;

import com.kamrul.blog.Config;
import com.kamrul.blog.MockRequest;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;
    private MockRequest mockRequest;

    @BeforeAll
    void setUp() throws Exception{
        mockRequest =  new MockRequest(mockMvc);
    }

    private void matchToLoggedInUser(ResultActions response) throws Exception{
        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.userName").value(Config.User.userName))
                .andExpect(jsonPath("$.email").value(Config.User.email))
                .andExpect(jsonPath("$.city").value("test_city"))
                .andExpect(jsonPath("$.country").value("test_country"))
                .andExpect(jsonPath("$.gender").value("test_gender"))
                .andExpect(jsonPath("$.firstName").value("test_firstname"))
                .andExpect(jsonPath("$.lastName").value("test_lastname"))
                .andExpect(jsonPath("$.userDescription").value("test_description"));
    }

    @Test
    @Order(1)
    void shouldGetUserUsingUserName() throws Exception{
        matchToLoggedInUser(mockRequest.get(String.format("/api/user/%s", Config.User.userName)));
    }

    @Test
    @Order(2)
    void shouldGetCurrentlyLoggedInUser() throws Exception{
        matchToLoggedInUser(mockRequest.get("/api/user/currentlyLoggedInUser"));
    }

    @Test
    @Order(300)
    void shouldDeleteLoggedInUser() throws Exception{
        mockRequest.delete("/api/user").andExpect(status().isOk());
    }
}