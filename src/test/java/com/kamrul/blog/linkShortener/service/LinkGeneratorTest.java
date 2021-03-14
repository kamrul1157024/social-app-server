package com.kamrul.blog.linkShortener.service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LinkGeneratorTest {

    @Test
    void getHashedLink() {

        String link="https://en.wikipedia.org/wiki/MurmurHash";
        LinkGenerator.getHashedLink(link);
    }
}