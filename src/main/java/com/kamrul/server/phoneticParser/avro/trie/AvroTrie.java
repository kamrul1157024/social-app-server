package com.kamrul.server.phoneticParser.avro.trie;



import com.kamrul.server.phoneticParser.avro.PhoneticLoader;
import com.kamrul.server.phoneticParser.avro.phonetic.Pattern;

import java.util.List;

public class AvroTrie {

    private final TrieNode head;
    private static volatile AvroTrie avroTrie=null;

    private void init(PhoneticLoader phoneticLoader) throws Exception {

        List<Pattern> patterns=phoneticLoader
                .getData()
                .getPatterns();

        for(int i=0;i<patterns.size();i++)
        {
            insert(patterns.get(i).getFind(),i);
        }

    }

    public Object clone() throws CloneNotSupportedException {
     throw new CloneNotSupportedException();
    }

    private AvroTrie() throws Exception {
        head=new TrieNode();
    }

    public static synchronized AvroTrie getInstance() throws Exception {
        if(avroTrie==null)
        {
            avroTrie=new AvroTrie();
        }
        return avroTrie;
    }


    public void setPhoneticLoader(PhoneticLoader phoneticLoader) throws Exception {
        init(phoneticLoader);
    }

    public void insert(String word, int patternPos)
    {
        TrieNode current=head;

        for (int i=0;i<word.length();i++)
        {
            char currentChar=word.charAt(i);
            if(current.getNextNode(currentChar)!=null)
            {
                current=current.getNextNode(currentChar);
            }
            else
            {
                TrieNode trieNode=new TrieNode(currentChar);
                current.setNextNode(currentChar,trieNode);
                current=trieNode;
            }
        }
        current.setEndPoint(true);
        current.setPatternPos(patternPos);
    }

    public boolean find(String word)
    {
        TrieNode current=head;

        for(int i=0;i<word.length();i++)
        {
            char currentChar=word.charAt(i);
            if(current.getNextNode(currentChar)==null) return false;

            current=current.getNextNode(currentChar);
        }
        return current.getEndPoint();
    }

    public int getPatternPos(String find)
    {
        TrieNode current=head;
        int lastMatchPattern=-1;
        for(int i=0;i<find.length();i++)
        {
            char currentChar=find.charAt(i);
            if(current.getNextNode(currentChar)!=null) {
                current = current.getNextNode(currentChar);
                lastMatchPattern=current.getPatternPos();
            }
            else
                return lastMatchPattern;
        }
        return lastMatchPattern;
    }


}
