package com.kamrul.server.controllers;

import com.kamrul.server.services.verify.PostVerifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

import static org.mockito.Mockito.mock;

@Profile("controller_test")
@Configuration
public class ControllerTestMockConfiguration {
    @Bean @Primary public PostVerifier getPostVerifier(){ return mock(PostVerifier.class); }
}