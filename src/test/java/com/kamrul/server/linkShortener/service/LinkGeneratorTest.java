package com.kamrul.server.linkShortener.service;

import org.junit.jupiter.api.Test;

class LinkGeneratorTest {

    @Test
    void getHashedLink() {

        String link="https://en.wikipedia.org/wiki/MurmurHash";
        LinkGenerator.getHashedLink(link);
    }
}