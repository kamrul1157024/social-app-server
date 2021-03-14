package com.omicronlab.avro.trie;
import java.util.HashMap;

public class TrieNode {
    private Character character;
    private HashMap<Character,TrieNode> nextNode;
    private int patternPos;
    private Boolean isEndPoint;

    public TrieNode() {
        this.nextNode=new HashMap<>();
    }

    public TrieNode(Character character) {
        this.character = character;
        this.nextNode=new HashMap<>();
    }


    public TrieNode getNextNode(Character character) {
        return nextNode.getOrDefault(character,null);
    }

    public void setNextNode(Character character,TrieNode trieNode) {
        nextNode.put(character,trieNode);
    }

    public int getPatternPos() {
        return patternPos;
    }

    public void setPatternPos(int patternPos) {
        this.patternPos = patternPos;
    }

    public Boolean getEndPoint() {
        return isEndPoint;
    }

    public void setEndPoint(Boolean endPoint) {
        isEndPoint = endPoint;
    }

}
