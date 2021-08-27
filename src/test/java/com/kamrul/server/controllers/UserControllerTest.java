package com.kamrul.server.controllers;

import com.kamrul.server.Config;
import com.kamrul.server.MockRequest;
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
                .andExpect(jsonPath("$.userName").value(mockRequest.getUserNameAndPassword().getLeft()))
                .andExpect(jsonPath("$.email").isString())
                .andExpect(jsonPath("$.city").isString())
                .andExpect(jsonPath("$.country").isString())
                .andExpect(jsonPath("$.gender").isString())
                .andExpect(jsonPath("$.firstName").isString())
                .andExpect(jsonPath("$.lastName").isString())
                .andExpect(jsonPath("$.userDescription").isString());
    }

    @Test
    @Order(1)
    void shouldGetUserUsingUserName() throws Exception{
        matchToLoggedInUser(mockRequest.get(String.format("/api/user/%s", mockRequest.getUserNameAndPassword().getLeft())));
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