package com.kamrul.blog.phoneticParser.avro;


import com.kamrul.blog.phoneticParser.avro.exception.NoPhoneticLoaderException;
import com.kamrul.blog.phoneticParser.avro.phonetic.Data;
import com.kamrul.blog.phoneticParser.avro.phonetic.Match;
import com.kamrul.blog.phoneticParser.avro.phonetic.Pattern;
import com.kamrul.blog.phoneticParser.avro.phonetic.Rule;
import com.kamrul.blog.phoneticParser.avro.trie.AvroTrie;

import java.util.ArrayList;
import java.util.List;

public class TriePhoneticParser {

    private static volatile TriePhoneticParser instance = null;
    private static PhoneticLoader loader = null;
    private static List<Pattern> patterns;
    private static String vowel = "";
    private static String consonant = "";
    private static String casesensitive = "";
    private boolean initialized = false;
    private static int maxPatternLength = 0;
    private static AvroTrie avroTrie;
    // Prevent initialization
    private TriePhoneticParser() {
        patterns = new ArrayList<Pattern>();
    }

    public Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }

    public static TriePhoneticParser getInstance() {
        if(instance == null) {
            synchronized (TriePhoneticParser.class) {
                if(instance == null) {
                    instance = new TriePhoneticParser();
                }
            }
        }
        return instance;
    }

    public synchronized void setLoader(PhoneticLoader loader) {
        TriePhoneticParser.loader = loader;
    }

    public synchronized void init() throws Exception {
        if(loader == null) {
            throw new NoPhoneticLoaderException();
        }
        Data data = loader.getData();
        patterns = data.getPatterns();
        avroTrie=AvroTrie.getInstance();
        avroTrie.setPhoneticLoader(loader);
        vowel = data.getVowel();
        consonant = data.getConsonant();
        casesensitive = data.getCasesensitive();
        maxPatternLength = patterns.get(0).getFind().length();
        initialized = true;
    }

    public String parse(String input) {

        if(initialized == false) {
            try {
                this.init();
            } catch(Exception e) {
                System.err.println(e);
                System.err.println("Please handle the exception by calling init mehotd");
                System.exit(0);
            }
        }

        String fixed = "";
        for(char c: input.toCharArray()) {
            if(this.isCaseSensitive(c)) {
                fixed += c;
            }
            else {
                fixed += Character.toLowerCase(c);
            }
        }

        String output = "";
        for(int cur = 0; cur < fixed.length(); ++cur) {
            int start = cur, end;

            boolean matched = false;
            int len;
            for(len = maxPatternLength; len > 0; --len) {
                end = start + len;
                if(end <= fixed.length()) {
                    String chunk = fixed.substring(start, end);

                        Integer patternPos = avroTrie.getPatternPos(chunk);
                        if (patternPos==-1) continue;
                        Pattern pattern= patterns.get(patternPos);
                        if(pattern.getFind().equals(chunk)) {
                            for(Rule rule: pattern.getRules()) {
                                boolean replace = true;

                                int chk = 0;

                                for(Match match: rule.getMatches()) {
                                    if(match.getType().equals("suffix")) {
                                        chk = end;
                                    }
                                    // Prefix
                                    else {
                                        chk = start - 1;
                                    }

                                    // Beginning
                                    if(match.getScope().equals("punctuation")) {
                                        if(
                                                ! (
                                                        (chk < 0 && match.getType().equals("prefix")) ||
                                                                (chk >= fixed.length() && match.getType().equals("suffix")) ||
                                                                this.isPunctuation(fixed.charAt(chk))
                                                ) ^ match.isNegative()
                                        ) {
                                            replace = false;
                                            break;
                                        }
                                    }
                                    // Vowel
                                    else if(match.getScope().equals("vowel")) {
                                        if(
                                                ! (
                                                        (
                                                                (chk >= 0 && match.getType().equals("prefix")) ||
                                                                        (chk < fixed.length() && match.getType().equals("suffix"))
                                                        ) &&
                                                                this.isVowel(fixed.charAt(chk))
                                                ) ^ match.isNegative()
                                        ) {
                                            replace = false;
                                            break;
                                        }
                                    }
                                    // Consonant
                                    else if(match.getScope().equals("consonant")) {
                                        if(
                                                ! (
                                                        (
                                                                (chk >= 0 && match.getType().equals("prefix")) ||
                                                                        (chk < fixed.length() && match.getType().equals("suffix"))
                                                        ) &&
                                                                this.isConsonant(fixed.charAt(chk))
                                                ) ^ match.isNegative()
                                        ) {
                                            replace = false;
                                            break;
                                        }
                                    }
                                    // Exact
                                    else if(match.getScope().equals("exact")) {
                                        int s, e;
                                        if(match.getType().equals("suffix")) {
                                            s = end;
                                            e = end + match.getValue().length();
                                        }
                                        // Prefix
                                        else {
                                            s = start - match.getValue().length();
                                            e = start;
                                        }
                                        if(!this.isExact(match.getValue(), fixed, s, e, match.isNegative())) {
                                            replace = false;
                                            break;
                                        }
                                    }
                                }

                                if(replace) {
                                    output += rule.getReplace();
                                    cur = end - 1;
                                    matched = true;
                                    break;
                                }

                            }

                            if(matched == true) break;

                            // Default
                            output += pattern.getReplace();
                            cur = end - 1;
                            matched = true;
                            break;
                        }

                    if(matched == true) break;
                }
            }

            if(!matched) {
                output += fixed.charAt(cur);
            }
            // System.out.printf("cur: %s, start: %s, end: %s, prev: %s\n", cur, start, end, prev);
        }
        return output;
    }

    private boolean isVowel(char c) {
        return ((vowel.indexOf(Character.toLowerCase(c)) >= 0));
    }

    private boolean isConsonant(char c) {
        return ((consonant.indexOf(Character.toLowerCase(c)) >= 0));
    }

    private boolean isPunctuation(char c) {
        return (!(this.isVowel(c) || this.isConsonant(c)));
    }

    private boolean isExact(String needle, String heystack, int start, int end, boolean not) {
        return ((start >= 0 && end < heystack.length() && heystack.substring(start, end).equals(needle)) ^ not);
    }

    private boolean isCaseSensitive(char c) {
        return (casesensitive.indexOf(Character.toLowerCase(c)) >= 0);
    }

}
