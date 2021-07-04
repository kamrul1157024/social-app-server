package com.kamrul.server;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

public class MockRequest {
    private final MockMvc mockMvc;
    private final String jwt;

    public MockRequest(MockMvc mockMvc) throws Exception {
        this.mockMvc = mockMvc;
        this.jwt = Utils.loginOrRegister(mockMvc);
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
                .content(Utils.asJsonString(body))
                .header("Authorization",String.format("Bearer %s",jwt))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));
    }

    public ResultActions put(String url, Object body) throws Exception {
        return this.mockMvc.perform(MockMvcRequestBuilders
                .put(url)
                .content(Utils.asJsonString(body))
                .header("Authorization",String.format("Bearer %s",jwt))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));
    }

    public ResultActions delete(String url, Object body) throws Exception {
        return this.mockMvc.perform(MockMvcRequestBuilders
                .delete(url)
                .content(Utils.asJsonString(body))
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
