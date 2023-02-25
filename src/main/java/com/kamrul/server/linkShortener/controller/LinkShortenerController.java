package com.kamrul.server.linkShortener.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@CrossOrigin
@RestController
@RequestMapping("api/shortLink")
public class LinkShortenerController {

    @GetMapping
    ResponseEntity<?> getShortenedURL(@RequestParam("link") String link)
    {
        return null;
    }

}
