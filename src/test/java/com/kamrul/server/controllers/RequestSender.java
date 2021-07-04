package com.kamrul.server.controllers;

import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

public class RequestSender {

    public static String POSTRequest(String url,String body,String jwt)
    {
        RestTemplate restTemplate=new RestTemplate();
        HttpHeaders httpHeaders=new HttpHeaders();
        httpHeaders.add("Authorization",jwt);
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request=new HttpEntity<>(body,httpHeaders);
        return restTemplate.postForObject(url,request,String.class);
    }

    public static HttpStatus DELETERequest(String url, String jwt)
    {
        RestTemplate restTemplate=new RestTemplate();
        HttpHeaders httpHeaders=new HttpHeaders();
        httpHeaders.add("Authorization",jwt);
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request= new HttpEntity<>(null,httpHeaders);
        return restTemplate.exchange(url, HttpMethod.DELETE,request,String.class).getStatusCode();
    }

}
