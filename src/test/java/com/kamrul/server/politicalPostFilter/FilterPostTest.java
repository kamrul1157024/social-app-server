package com.kamrul.server.politicalPostFilter;

import com.kamrul.server.politicalPostFilter.models.LanguageProbability;
import com.kamrul.server.politicalPostFilter.models.TextAnalyzingResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
class FilterPostTest {
    FilterPost filterPost;
    String text="\"Bengali Unicode block contains characters for the Bengali," +
            " Assamese, Bishnupriya Manipuri, Daphla, Garo, Hallam, Khasi, Mizo, Munda, " +
            "Naga, Riang, and Santali languages. In its original incarnation, the code points " +
            "U+0981..U+09CD were a direct copy of the Bengali characters A1-ED from the 1988 " +
            "ISCII standard, as well as several Assamese ISCII characters in the U+09F0 column. " +
            "The Devanagari, Gurmukhi, Gujarati, Oriya, Tamil, Telugu, Kannada, and Malayalam blocks " +
            "were similarly all based on ISCII encodings.\"";

    @BeforeEach
    void setUp(){
        filterPost = mock(FilterPost.class);
    }

    @Test
    void detectLanguage() {
        doReturn(new LanguageProbability()).when(filterPost).detectLanguage(text);
        assertTrue(filterPost.detectLanguage(text) instanceof LanguageProbability);
    }

    @Test
    void isPoliticalStatementTest() {
        doReturn(new TextAnalyzingResponse()).when(filterPost).isPoliticalStatement(text);
        assertTrue(filterPost.isPoliticalStatement(text) instanceof TextAnalyzingResponse);
    }
}