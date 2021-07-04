package com.kamrul.blog;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@WebMvcTest
class BlogApplicationTests {

    @Autowired
    public MockMvc mockMvc;

    @Test
    void contextLoads() throws Exception{
    }

}
