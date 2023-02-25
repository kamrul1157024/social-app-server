package com.kamrul.server.services.draftJS;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class DraftJSTextRequestParsingTest {

    @Autowired
    DraftJSTextParsing draftJSTextParsing;

    @Test
    void allTextTest() {
        String draftJSRawJson="{\n" +
                "  \"blocks\": [\n" +
                "    {\n" +
                "      \"key\": \"ekceu\",\n" +
                "      \"text\": \"Testing The DraftJSRaw\",\n" +
                "      \"type\": \"header-two\",\n" +
                "      \"depth\": 0,\n" +
                "      \"inlineStyleRanges\": [],\n" +
                "      \"entityRanges\": [],\n" +
                "      \"data\": {}\n" +
                "    }]}";
        assertEquals("Testing The DraftJSRaw",draftJSTextParsing.getAllText(draftJSRawJson));
    }



}