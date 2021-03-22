package com.kamrul.blog.controllers;
import com.kamrul.blog.models.user.User;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.*;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class IntegrationTest {

    @LocalServerPort
    private int port;
    private String serverAddress;

    private String jwt;

    @BeforeAll
    void setUp() throws JSONException {
        serverAddress="http://localhost:"+port;
        String user=createUser();
        String expectedResponse="{\n" +
                "    \"userName\": \"testUserName\",\n" +
                "    \"firstName\": \"testFirstName\",\n" +
                "    \"lastName\": \"testLastName\",\n" +
                "    \"email\": \"test@demo.com\",\n" +
                "    \"profilePicture\": \"\",\n" +
                "    \"city\": \"Dhaka\",\n" +
                "    \"country\": \"Bangladesh\",\n" +
                "    \"gender\": \"male\",\n" +
                "    \"totalNumberOfFollower\": 0,\n" +
                "    \"totalNumberOfUserFollowed\": 0,\n" +
                "    \"userDescription\": null,\n" +
                "    \"followedByCurrentlyLoggedInUser\": null,\n" +
                "    \"emailVisible\": true,\n" +
                "    \"emailVerified\": false\n" +
                "}";
        JSONAssert.assertEquals(expectedResponse,user,false);
    }

    @AfterAll
    void afterAllTest()
    {
        deleteUser();
    }

    private String createUser()
    {
        String createUserURL= serverAddress+"/auth/register";
        String userInformation= "{\n" +
                "    \"userName\":\"testUserName\",\n" +
                "    \"firstName\":\"testFirstName\",\n" +
                "    \"lastName\": \"testLastName\",\n" +
                "    \"password\" :\"1234567\",\n" +
                "    \"dateOfBirth\":\"08/06/1997\",\n" +
                "    \"email\":\"test@demo.com\",\n" +
                "    \"profilePicture\":\"\",\n" +
                "    \"city\":\"Dhaka\",\n" +
                "    \"country\":\"Bangladesh\",\n" +
                "    \"emailVisible\":\"true\",\n" +
                "    \"gender\":\"male\"\n" +
                "}";
        return RequestSender.POSTRequest(
                createUserURL,
                userInformation,
                null
        );
    }

    private void logIn() throws JSONException {
        String loginURL=serverAddress+"/auth/authenticate";
        String logInCredentials="{\n" +
                "    \"userName\":\"testUserName\",\n" +
                "    \"password\":\"1234567\"\n" +
                "}";
        this.jwt= "Bearer "+new JSONObject(RequestSender.POSTRequest(
                loginURL,
                logInCredentials,
                null
        )).getString("jwt");
    }

    private HttpStatus deleteUser()
    {
        String deleteURL=serverAddress+"/api/user";
        return RequestSender.DELETERequest(deleteURL,this.jwt);
    }


}
