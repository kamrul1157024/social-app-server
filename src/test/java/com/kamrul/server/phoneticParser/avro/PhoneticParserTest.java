package com.kamrul.server.phoneticParser.avro;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PhoneticParserTest {

    PhoneticParser phoneticParser;

    @BeforeAll
    void setUp() throws Exception {
        phoneticParser=PhoneticParser.getInstance();
    }

    @Test
    void shouldNotBeCloneAble()
    {
        assertThrows(Exception.class,()-> phoneticParser.clone());
    }

    @Test
    void shouldBeInstanceOfPhoneticParser()
    {
        assertEquals(PhoneticParser.class,phoneticParser.getClass());
    }

    @Test
    void phoneticParserAutoCorrectedStringTest()
    {
        String text="aTA gase Tota Pakhi";
        assertEquals(text,phoneticParser.autoCorrectString(text));
    }

    @Test
    void phoneParserAutoWithAutoCorrectTest()
    {
        assertEquals("আমি আইফোন  কিনব",
                phoneticParser.parseBangla("Ami iphone Kinbo",true));

        assertEquals("আম পাতা জরা জরা মারব চাবুক চরব ঘোড়া ",
                phoneticParser.parseBangla(
                        "am pata jora jora marbo cabuk corbo ghora " ,true)
        );

    }

    @Test
    void phoneticParserWithoutAutoCorrectTest()
    {
        assertEquals("আমি ইফনে কিনব",
                phoneticParser.parseBangla("Ami iphone Kinbo",false));
    }

}