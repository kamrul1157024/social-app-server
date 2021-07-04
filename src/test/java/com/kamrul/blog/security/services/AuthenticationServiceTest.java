package com.kamrul.blog.security.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kamrul.blog.Config;
import com.kamrul.blog.Utils;
import com.kamrul.blog.dto.UserDTO;
import com.kamrul.blog.security.models.AuthenticationRequest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Date;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static com.kamrul.blog.Utils.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AuthenticationServiceTest {

    @Autowired
    private MockMvc mockMvc;

    @BeforeAll
    void setUp() throws Exception {
        loginOrRegister(mockMvc);
    }

    @Test
    void shouldRegisterUserProperly() throws Exception {
        String randomUserName = Utils.getRandomString();
        String randomEmail =  Utils.getRandomString()+"@test.com";
        UserDTO userDTO =  new UserDTO();
        userDTO.setUserName(randomUserName);
        userDTO.setEmail(randomEmail);
        userDTO.setPassword("test242%24jj@");
        userDTO.setCity("test_city");
        userDTO.setCountry("test_country");
        userDTO.setEmailVerified(Boolean.FALSE);
        userDTO.setDateOfBirth(new Date());
        userDTO.setEmailVisible(Boolean.TRUE);
        userDTO.setGender("test_gender");
        userDTO.setFirstName("test_firstname");
        userDTO.setLastName("test_lastname");
        userDTO.setUserDescription("test_description");
        mockMvc.perform(MockMvcRequestBuilders
                .post("/auth/register")
                .content(addField(asJsonString(userDTO),"password","\"test123\""))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.userName").value(randomUserName))
                .andExpect(jsonPath("$.email").value(randomEmail))
                .andExpect(jsonPath("$.city").value("test_city"))
                .andExpect(jsonPath("$.country").value("test_country"))
                .andExpect(jsonPath("$.gender").value("test_gender"))
                .andExpect(jsonPath("$.firstName").value("test_firstname"))
                .andExpect(jsonPath("$.lastName").value("test_lastname"))
                .andExpect(jsonPath("$.userDescription").value("test_description"));
    }

    @Test
    void ShouldLogInProperly() throws Exception{
        AuthenticationRequest authenticationRequest = new AuthenticationRequest();
        authenticationRequest.setUserName(Config.User.userName);
        authenticationRequest.setPassword(Config.User.password);
        mockMvc.perform(MockMvcRequestBuilders
                .post("/auth/authenticate")
                .content(asJsonString(authenticationRequest))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.jwt").exists())
                .andExpect(jsonPath("$.jwt").isString());
    }
}