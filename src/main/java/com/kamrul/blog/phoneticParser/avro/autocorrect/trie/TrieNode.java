package com.omicronlab.avro.autocorrect.trie;

import java.util.HashMap;

public class TrieNode {
    private Character character;
    private HashMap<Character,TrieNode> nextNodes;
    private boolean isEnd=false;
    private String replace=null;

    public TrieNode()
    {
        nextNodes=new HashMap<>();
    }

    public TrieNode(Character character)
    {
        this.character=character;
        nextNodes=new HashMap<>();
    }

    public Character getCharacter() {
        return character;
    }


    public HashMap<Character, TrieNode> getNextNodes() {
        return nextNodes;
    }

    public TrieNode getNextNode(Character character) {
        return nextNodes.getOrDefault(character,null);
    }

    public void setNextNode(Character character, TrieNode trieNode) {
        nextNodes.put(character,trieNode);
    }

    public boolean isEnd() {
        return isEnd;
    }

    public void setEnd(boolean end) {
        isEnd = end;
    }

    public String getReplace() {
        return replace;
    }

    public void setReplace(String replace) {
        this.replace = replace;
    }
}
