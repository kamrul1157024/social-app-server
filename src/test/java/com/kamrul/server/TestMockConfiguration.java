package com.kamrul.server;

import com.kamrul.server.politicalPostFilter.FilterPost;
import com.kamrul.server.services.draftJS.DraftJSTextParsing;
import com.kamrul.server.services.verify.PostVerifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

import static org.mockito.Mockito.mock;

@Profile("test")
@Configuration
public class TestMockConfiguration {
    @Bean @Primary public DraftJSTextParsing getDraftJSTextParsing(){ return mock(DraftJSTextParsing.class); }
    @Bean @Primary public FilterPost getFilterPost(){ return mock(FilterPost.class); }
    @Bean @Primary public PostVerifier getPostVerifier(){ return mock(PostVerifier.class); }
}
