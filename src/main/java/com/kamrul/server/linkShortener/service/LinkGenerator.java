package com.kamrul.server.linkShortener.service;

import org.apache.commons.codec.digest.MurmurHash3;

import java.nio.charset.StandardCharsets;

public class LinkGenerator {


    public static String getHashedLink(String link)
    {
        long[] hashedLink= MurmurHash3.hash128x64(link.getBytes(StandardCharsets.UTF_8));
        String hashedString="";
        return null;
    }

}
