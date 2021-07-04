package com.kamrul.server.politicalPostFilter.textProccessor;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TextRequestProcessorTest {


    @Test
    void extractEnglishCharacters() {
        String text="করোনা সংকটে গত বছরের মে AB মাসে বেতন কমায় I বিমান বাংলাদেশ এয়ারলাইনস। এখন তারা Love বেতনের এই কর্তন ১০ To শতাংশ কমিয়ে আনার do কথা THiS ভাবছে।";
        Assertions.assertEquals(
                "AB I Love To do THiS",
                TextProcessor.extractEnglishCharacters(text));
    }

    @Test
    void extractBanglaCharacters()
    {
        String text="করোনা সংকটে গত বছরের মে AB মাসে বেতন কমায় I বিমান বাংলাদেশ এয়ারলাইনস। এখন তারা Love বেতনের এই কর্তন ১০ To শতাংশ কমিয়ে আনার do কথা THiS ভাবছে।";
        assertEquals(
                "করোনা সংকটে গত বছরের মে মাসে বেতন কমায় বিমান বাংলাদেশ এয়ারলাইনস এখন তারা বেতনের এই কর্তন ১০ শতাংশ কমিয়ে আনার কথা ভাবছে",
                TextProcessor.extractBanglaCharacters(text));
    }

    @Test
    void convertBanglishToPhonetic() throws Exception {
        String text="iphoner dam onk Taka!";
        System.out.println(TextProcessor.convertBanglishToPhonetic(text));
    }
}