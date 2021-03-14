package com.omicronlab.avro;

import com.omicronlab.avro.autocorrect.AutoCorrectLoader;
import com.omicronlab.avro.autocorrect.trie.AutoCorrectTrie;
import com.omicronlab.avro.trie.AvroTrie;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class PhoneticParser {

    private static PhoneticParser phoneticParser=null;
    private static PhoneticLoader phoneticLoader=null;
    private static AvroTrie avroTrie=null;
    private static AutoCorrectLoader autoCorrectLoader=null;
    private static AutoCorrectTrie autoCorrectTrie=null;
    private static TriePhoneticParser triePhoneticParser=null;
    private Object CloneNotSupportedException;

    private PhoneticParser() throws Exception {

        phoneticLoader=new PhoneticXmlLoader();
        avroTrie=AvroTrie.getInstance();
        avroTrie.setPhoneticLoader(phoneticLoader);

        autoCorrectLoader=new AutoCorrectLoader();
        autoCorrectTrie=AutoCorrectTrie.getInstance();
        autoCorrectTrie.setAutoCorrectLoader(autoCorrectLoader);

        triePhoneticParser=TriePhoneticParser.getInstance();
        triePhoneticParser.setLoader(phoneticLoader);
    }

    public Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }

    public static synchronized PhoneticParser getInstance()
            throws Exception {
        if(phoneticParser==null)
        {
            phoneticParser=new PhoneticParser();
        }
        return phoneticParser;
    }

    public String parseBangla(String banglishText,boolean AUTO_CORRECT_ENABLE)
    {
        if(AUTO_CORRECT_ENABLE)
            banglishText=autoCorrectString(banglishText);

        String banglaText=triePhoneticParser.parse(banglishText);
        return banglaText;
    }

    private String autoCorrectString(String text)
    {
        String[] words= sentenceSplit(text);

        List<String> autoCorrectedWords=
                Arrays
                        .stream(words).map(word->autoCorrectTrie.getReplacement(word))
                        .collect(Collectors.toList());

        String autoCorrectedText=stringListToSentence(autoCorrectedWords);
        return autoCorrectedText;
    }

    private String[] sentenceSplit(String text)
    {
        return text.split("\\s+");
    }

    private String stringListToSentence(List<String> words)
    {
     StringBuilder sentence = new StringBuilder();
     words.forEach(word->{
         sentence.append(word);
         sentence.append(" ");
     });
     return sentence.toString();
    }



}
