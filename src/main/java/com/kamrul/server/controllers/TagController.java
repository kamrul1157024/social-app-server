package com.kamrul.server.controllers;

import com.kamrul.server.models.post.Post;
import com.kamrul.server.models.tag.Tag;
import com.kamrul.server.repositories.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/tag")
public class TagController {

    @Autowired
    TagRepository tagRepository;
    @GetMapping("/posts")
    ResponseEntity<?> getPostsByPostTags(@RequestParam("postTags") String postTags) {
        List<String> postTagsList=Arrays.asList(postTags.split(","));
        List<Post> posts= tagRepository.getPostTagByPostTagNames(postTagsList);
        return new ResponseEntity<>(posts, HttpStatus.OK);
    }

    @GetMapping("/allTags")
    ResponseEntity<?> getAllTags() {
        List<Tag> tags=tagRepository.getAllTags();
        return new ResponseEntity<>(tags,HttpStatus.OK);
    }

    @PostMapping
    ResponseEntity<?> addNewTag(@RequestBody Tag tag) {
        tagRepository.save(new Tag(tag.getTagName()));
        Tag generatedTag=tagRepository.getTagByName(tag.getTagName());
        return new ResponseEntity<>(generatedTag,HttpStatus.OK);
    }
}
