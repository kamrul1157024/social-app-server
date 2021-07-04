package com.kamrul.server.phoneticParser.avro;

import com.kamrul.server.phoneticParser.avro.autocorrect.AutoCorrectLoader;
import com.kamrul.server.phoneticParser.avro.autocorrect.trie.AutoCorrectTrie;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AutoCorrectTrieTest {

    AutoCorrectTrie trie;

    @BeforeAll
    void setUp() throws Exception {
        trie=AutoCorrectTrie.getInstance();
        AutoCorrectLoader autoCorrectLoader=new AutoCorrectLoader();
        trie.setAutoCorrectLoader(autoCorrectLoader);
    }

    @Test
    void checkInstance()
    {
        assertEquals(AutoCorrectTrie.class,trie.getClass());
    }

    @Test
    void shouldNotBeCloneable()
    {
        assertThrows(Exception.class,()->trie.clone());
    }

    @Test
    void trieReplacementChecking()
    {
        assertEquals("erOplen ",trie.getReplacement("aeroplane"));
    }

    @Test
    void trieReplacementCheckingWithUnmatchedSuffix()
    {
        assertEquals("erOplen ke",trie.getReplacement("aeroplaneke"));
    }

}