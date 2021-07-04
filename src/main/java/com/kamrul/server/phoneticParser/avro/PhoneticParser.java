package com.kamrul.server.phoneticParser.avro;



import com.kamrul.server.phoneticParser.avro.autocorrect.AutoCorrectLoader;
import com.kamrul.server.phoneticParser.avro.autocorrect.trie.AutoCorrectTrie;
import com.kamrul.server.phoneticParser.avro.trie.AvroTrie;

import java.util.Arrays;
import java.util.stream.Collectors;

public class PhoneticParser {

    private static PhoneticParser phoneticParser=null;
    private static PhoneticLoader phoneticLoader=null;
    private static AvroTrie avroTrie=null;
    private static AutoCorrectLoader autoCorrectLoader=null;
    private static AutoCorrectTrie autoCorrectTrie=null;
    private static TriePhoneticParser triePhoneticParser=null;

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

    public String autoCorrectString(String text)
    {
        String[] words= text.split("\\s+");

        return Arrays
                .stream(words)
                .map(word->autoCorrectTrie.getReplacement(word))
                .collect(Collectors.joining(" "));

    }

}
