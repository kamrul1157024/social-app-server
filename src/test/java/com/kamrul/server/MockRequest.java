package com.kamrul.server;

import com.github.javafaker.Faker;
import com.kamrul.server.models.user.User;
import com.kamrul.server.repositories.UserRepository;
import com.kamrul.server.security.jwt.JWTUtil;
import lombok.Getter;
import lombok.SneakyThrows;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@Getter
public class MockRequest {
    private final MockMvc mockMvc;
    private String jwt;
    private final  ImmutablePair<String,String> userNameAndPassword;
    private final UserRepository userRepository;

    public MockRequest(MockMvc mockMvc,UserRepository userRepository) {
        Faker faker = new Faker();
        this.mockMvc = mockMvc;
        this.userRepository = userRepository;
        this.userNameAndPassword = new ImmutablePair<>(faker.name().username(),Config.User.password);
        try {
            this.jwt = MockUserCreator.loginOrRegister(mockMvc,userNameAndPassword);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    @SneakyThrows
    public Long getUserId(){
        return JWTUtil.getUserIdFromJwt(String.format("Bearer %s",jwt));
    }

    public User getUser(){
        return userRepository.findById(getUserId()).get();
    }

    public ResultActions get(String url) throws Exception {
        return this.mockMvc.perform(MockMvcRequestBuilders
                .get(url)
                .header("Authorization",String.format("Bearer %s",jwt))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));
    }

    public ResultActions getAsUnAuthorized(String url) throws Exception {
        return this.mockMvc.perform(MockMvcRequestBuilders
                .get(url)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));
    }

    public ResultActions post(String url, Object body) throws Exception {
        return this.mockMvc.perform(MockMvcRequestBuilders
                .post(url)
                .content(MockUserCreator.asJsonString(body))
                .header("Authorization",String.format("Bearer %s",jwt))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));
    }

    public ResultActions put(String url, Object body) throws Exception {
        return this.mockMvc.perform(MockMvcRequestBuilders
                .put(url)
                .content(MockUserCreator.asJsonString(body))
                .header("Authorization",String.format("Bearer %s",jwt))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));
    }

    public ResultActions delete(String url, Object body) throws Exception {
        return this.mockMvc.perform(MockMvcRequestBuilders
                .delete(url)
                .content(MockUserCreator.asJsonString(body))
                .header("Authorization",String.format("Bearer %s",jwt))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));
    }

    public ResultActions delete(String url) throws Exception {
        return this.mockMvc.perform(MockMvcRequestBuilders
                .delete(url)
                .header("Authorization",String.format("Bearer %s",jwt))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));
    }
}
