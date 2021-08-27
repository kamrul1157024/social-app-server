package com.kamrul.server;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import com.kamrul.server.dto.UserDTO;
import com.kamrul.server.security.models.AuthenticationRequest;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Date;

public class Utils {
    private static Faker faker= new Faker();
    public static String asJsonString(final Object object) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(object);
    }

    public static String addField(String json,String fieldName,String fieldValue){
        return json.substring(0,json.length()-1).concat(",\""+fieldName+"\":"+fieldValue+"}");
    }

    public static String getRandomString(){
        return "test"+Math.random()*100000000;
    }

    public static String login(MockMvc mockMvc,ImmutablePair<String,String> usernameAndPassword) throws Exception{
        AuthenticationRequest authenticationRequest = new AuthenticationRequest();
        String userName = usernameAndPassword.getLeft();
        String password = usernameAndPassword.getRight();
        authenticationRequest.setUserName(userName);
        authenticationRequest.setPassword(password);
        JSONObject loginResponse = new JSONObject(
                mockMvc.perform(MockMvcRequestBuilders
                        .post("/auth/authenticate")
                        .content(asJsonString(authenticationRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                        .andReturn().getResponse().getContentAsString());
        System.out.println(loginResponse);
        return loginResponse.getString("jwt");
    }

    public static void register(MockMvc mockMvc,ImmutablePair<String,String> userNameAndPassword) throws Exception{
        String userName = userNameAndPassword.getLeft();
        String password = userNameAndPassword.getRight();
        String email = String.format("%s@gmail.com",userName);
        UserDTO userDTO = new UserDTO();
        userDTO.setUserName(userName);
        userDTO.setEmail(email);
        userDTO.setPassword(password);
        userDTO.setCity(faker.address().cityName());
        userDTO.setCountry(faker.address().country());
        userDTO.setEmailVerified(Boolean.FALSE);
        userDTO.setDateOfBirth(faker.date().birthday());
        userDTO.setEmailVisible(Boolean.TRUE);
        userDTO.setGender("male");
        userDTO.setFirstName(faker.name().firstName());
        userDTO.setLastName(faker.name().lastName());
        userDTO.setUserDescription(faker.harryPotter().book());
        mockMvc.perform(MockMvcRequestBuilders
                .post("/auth/register")
                .content(addField(asJsonString(userDTO),"password",String.format("\"%s\"",Config.User.password)))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));
    }

    public static Boolean hasUser(String userName,MockMvc mockMvc) throws Exception{
        try{
            JSONObject response =  new JSONObject(
                    mockMvc.perform(MockMvcRequestBuilders
                            .get(String.format("/api/user/%s",userName))
                            .accept(MediaType.APPLICATION_JSON))
                            .andReturn().getResponse().getContentAsString());
            response.get("userName");
            return true;
        }
        catch (JSONException e){
            return false;
        }
    }

    public static String loginOrRegister(MockMvc mockMvc,ImmutablePair<String,String> userNameAndPassword) throws Exception{
        String userName = userNameAndPassword.getLeft();
        if(!hasUser(userName, mockMvc)) register(mockMvc,userNameAndPassword);
        return login(mockMvc,userNameAndPassword);
    }
}
