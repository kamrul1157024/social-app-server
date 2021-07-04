package com.kamrul.blog;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kamrul.blog.dto.UserDTO;
import com.kamrul.blog.security.models.AuthenticationRequest;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Date;

public class Utils {
    public static String asJsonString(final Object object) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(object);
    }

    public static String addField(String json,String fieldName,String fieldValue){
        return json.substring(0,json.length()-1).concat(",\""+fieldName+"\":"+fieldValue+"}");
    }

    public static String getRandomString(){
        return "test"+Math.random()*100000000;
    }

    public static String login(MockMvc mockMvc) throws Exception{
        AuthenticationRequest authenticationRequest = new AuthenticationRequest();
        authenticationRequest.setUserName(Config.User.userName);
        authenticationRequest.setPassword(Config.User.password);
        JSONObject loginResponse = new JSONObject(
                mockMvc.perform(MockMvcRequestBuilders
                        .post("/auth/authenticate")
                        .content(asJsonString(authenticationRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                        .andReturn().getResponse().getContentAsString());
        return loginResponse.getString("jwt");
    }

    public static void register(MockMvc mockMvc) throws Exception{
        UserDTO userDTO =  new UserDTO();
        userDTO.setUserName(Config.User.userName);
        userDTO.setEmail(Config.User.email);
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

    public static String loginOrRegister(MockMvc mockMvc) throws Exception{
        if(!hasUser(Config.User.userName, mockMvc)) register(mockMvc);
        return login(mockMvc);
    }
}
