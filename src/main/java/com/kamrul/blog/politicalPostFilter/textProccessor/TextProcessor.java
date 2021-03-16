package com.kamrul.blog.politicalPostFilter.textProccessor;

import com.kamrul.blog.phoneticParser.avro.PhoneticParser;
import lombok.SneakyThrows;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class TextProcessor {

    public static String extractEnglishCharacters(String text)
    {
        String pattern="[a-zA-Z]+";
        Pattern englishSymbolPattern= Pattern.compile(pattern);
        Matcher matcher= englishSymbolPattern.matcher(text);
        return matcher
                .results()
                .map(matchResult -> matchResult.group())
                .collect(Collectors.joining(" "));
    }

    public static String extractBanglaCharacters(String text)
    {
        String pattern="[\\u0980-\\u09FF]+";
        Pattern banglaCharacterSymbolPattern=Pattern.compile(pattern);
        Matcher matcher=banglaCharacterSymbolPattern.matcher(text);

        return matcher
                .results()
                .map(matchResult -> matchResult.group())
                .collect(Collectors.joining(" "));
    }

    @SneakyThrows
    public static String convertBanglishToPhonetic(String text)  {
        PhoneticParser phoneticParser=PhoneticParser.getInstance();
        return phoneticParser.parseBangla(text,true);
    }

    public static String extractBanglishInBangla(String text)
    {
        return convertBanglishToPhonetic(extractEnglishCharacters(text));
    }
}
