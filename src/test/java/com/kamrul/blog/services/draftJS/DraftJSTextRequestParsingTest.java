package com.kamrul.blog.services.draftJS;

import com.kamrul.blog.services.FileReading;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;

import static org.junit.jupiter.api.Assertions.*;


class DraftJSTextRequestParsingTest {

    DraftJSTextParsing draftJSTextParsing;

    @BeforeEach
    void setUp()
    {
        String draftJSRawJsonPath="src/test/java/com/kamrul/blog/services/draftJS/draftJSRaw.json";
        this.draftJSTextParsing=new DraftJSTextParsing(FileReading.getAllText(draftJSRawJsonPath));
    }

    @Test
    void allTextTest()
    {
        assertEquals("রাজস্ব বাড়ছে সরকারের",draftJSTextParsing.getAllText().substring(0,20));
    }



}