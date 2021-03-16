package com.kamrul.blog.politicalPostFilter;

import com.kamrul.blog.politicalPostFilter.models.LanguageProbability;
import com.kamrul.blog.politicalPostFilter.models.TextAnalyzingResponse;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.context.annotation.ApplicationScope;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FilterPostTest {

    @Autowired
    FilterPost filterPost;

    String text="\"Bengali Unicode block contains characters for the Bengali," +
            " Assamese, Bishnupriya Manipuri, Daphla, Garo, Hallam, Khasi, Mizo, Munda, " +
            "Naga, Riang, and Santali languages. In its original incarnation, the code points " +
            "U+0981..U+09CD were a direct copy of the Bengali characters A1-ED from the 1988 " +
            "ISCII standard, as well as several Assamese ISCII characters in the U+09F0 column. " +
            "The Devanagari, Gurmukhi, Gujarati, Oriya, Tamil, Telugu, Kannada, and Malayalam blocks " +
            "were similarly all based on ISCII encodings.\"";

    @Test
    void detectLanguage() {

        assertTrue(filterPost.detectLanguage(text) instanceof LanguageProbability);
    }

    @Test
    void isPoliticalStatementTest()
    {
        assertTrue(filterPost.isPoliticalStatement(text) instanceof TextAnalyzingResponse);
    }
}