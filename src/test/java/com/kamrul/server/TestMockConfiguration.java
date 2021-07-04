package com.kamrul.server;

import com.kamrul.server.politicalPostFilter.FilterPost;
import com.kamrul.server.services.draftJS.DraftJSTextParsing;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Profile("test")
@Configuration
public class TestMockConfiguration {

    @Bean
    @Primary
    public DraftJSTextParsing getDraftJSTextParsing(){
        return mock(DraftJSTextParsing.class);
    }

    @Bean
    @Primary
    public FilterPost getFilterPost(){
        return mock(FilterPost.class);
    }
}
