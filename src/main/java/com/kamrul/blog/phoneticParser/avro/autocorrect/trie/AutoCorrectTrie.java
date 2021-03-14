package com.kamrul.blog.phoneticParser.avro.autocorrect.trie;

import com.kamrul.blog.phoneticParser.avro.autocorrect.AutoCorrectLoader;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.Locale;


public class AutoCorrectTrie {
    private final TrieNode head;
    private AutoCorrectLoader autoCorrectLoader;
    private static volatile AutoCorrectTrie autoCorrectTrie=null;

    private void init(AutoCorrectLoader autoCorrectLoader)  {

        JSONObject jsonDict=autoCorrectLoader.getJsonDict();

        Iterator<String> jsonDictIterator=jsonDict.keys();


        while (jsonDictIterator.hasNext())
        {
            String key= jsonDictIterator.next();
            insert(key, (String) jsonDict.get(key));
        }

    }

    public Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }

    private AutoCorrectTrie() throws Exception {
        head=new TrieNode();
    }

    public static synchronized AutoCorrectTrie getInstance() throws Exception {
        if(autoCorrectTrie==null)
        {
            autoCorrectTrie=new AutoCorrectTrie();
        }
        return autoCorrectTrie;
    }


    public void setAutoCorrectLoader(AutoCorrectLoader autoCorrectLoader) throws Exception {
        this.autoCorrectLoader= autoCorrectLoader;
        init(autoCorrectLoader);
    }

    public void insert(String word,String replacement)
    {

        word=word.toLowerCase(Locale.ROOT);
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
        current.setEnd(true);
        current.setReplace(replacement);
    }


    public String getReplacement(String find)
    {
        find=find.toLowerCase(Locale.ROOT);
        TrieNode current=head;
        for(int i=0;i<find.length();i++)
        {
            char currentChar=find.charAt(i);
            if(current.getNextNode(currentChar)!=null) {
                current = current.getNextNode(currentChar);
            }
            else
                return find;
        }
        if (current.isEnd()) return current.getReplace();
        else
            return find;
    }

}
