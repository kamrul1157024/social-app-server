package com.kamrul.blog.linkShortener.controller;


import org.apache.commons.codec.digest.MurmurHash3;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;

@CrossOrigin
@RestController
@RequestMapping("api/shortLink")
public class LinkShortenerController {

    @GetMapping
    ResponseEntity<?> getShortenedURL(@RequestParam("link") String link)
    {
        long[] hashedLinkLong= MurmurHash3.hash128x64(link.getBytes(StandardCharsets.UTF_8));
        return null;
    }

}
