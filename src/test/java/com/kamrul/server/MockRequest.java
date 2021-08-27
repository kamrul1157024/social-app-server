package com.kamrul.server;

import com.github.javafaker.Faker;
import com.kamrul.server.fixtures.UserFixture;
import lombok.Getter;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@Getter
public class MockRequest {
    private final MockMvc mockMvc;
    private final String jwt;
    private final  ImmutablePair<String,String> userNameAndPassword;

    public MockRequest(MockMvc mockMvc) throws Exception {
        Faker faker = new Faker();
        this.mockMvc = mockMvc;
        userNameAndPassword = new ImmutablePair<>(faker.name().username()+ UserFixture.getRandomString(),Config.User.password);
        this.jwt = UserFixture.loginOrRegister(mockMvc,userNameAndPassword);
    }

    public ResultActions get(String url) throws Exception {
        return this.mockMvc.perform(MockMvcRequestBuilders
                .get(url)
                .header("Authorization",String.format("Bearer %s",jwt))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));
    }

    public ResultActions post(String url, Object body) throws Exception {
        return this.mockMvc.perform(MockMvcRequestBuilders
                .post(url)
                .content(UserFixture.asJsonString(body))
                .header("Authorization",String.format("Bearer %s",jwt))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));
    }

    public ResultActions put(String url, Object body) throws Exception {
        return this.mockMvc.perform(MockMvcRequestBuilders
                .put(url)
                .content(UserFixture.asJsonString(body))
                .header("Authorization",String.format("Bearer %s",jwt))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));
    }

    public ResultActions delete(String url, Object body) throws Exception {
        return this.mockMvc.perform(MockMvcRequestBuilders
                .delete(url)
                .content(UserFixture.asJsonString(body))
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
