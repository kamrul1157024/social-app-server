package com.kamrul.blog.controllers;

import com.kamrul.blog.dto.PostDTO;
import com.kamrul.blog.dto.UpVoteDTO;
import com.kamrul.blog.exception.ResourceNotFoundException;
import com.kamrul.blog.exception.UnauthorizedException;
import com.kamrul.blog.models.Post;
import com.kamrul.blog.models.UpVote;
import com.kamrul.blog.models.User;
import com.kamrul.blog.repositories.GeneralQueryRepository;
import com.kamrul.blog.repositories.PostRepository;
import com.kamrul.blog.repositories.UpVoteRepository;
import com.kamrul.blog.repositories.UserRepository;
import com.kamrul.blog.utils.Converters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Optional;

import static com.kamrul.blog.utils.GeneralResponseMSG.*;

@CrossOrigin
@RestController
@RequestMapping("/api/upvote")
public class UpVoteController {

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UpVoteRepository upVoteRepository;

    @PutMapping
    ResponseEntity<?> upVotePostByUser(@RequestBody UpVoteDTO upVoteDTO)
            throws UnauthorizedException, ResourceNotFoundException {

        Post post=GeneralQueryRepository.getByID(
                postRepository,
                upVoteDTO.getPostId(),
                POST_NOT_FOUND_MSG
        );
        User user=GeneralQueryRepository.getByID(
                userRepository,
                GeneralQueryRepository.getCurrentlyLoggedInUserId(),
                USER_NOT_FOUND_MSG
        );

        Optional<UpVote> upVote=upVoteRepository.
                findUpVoteByUserIdAndPostID(user.getUserId(),post.getPostId());



        if(!upVote.isPresent())
        {
            UpVote upvote=new UpVote(user,post);
            post.setTotalUpVotes(post.getTotalUpVotes()+1);
            upVoteRepository.save(upvote);
        }
        else
        {
            post.setTotalUpVotes(post.getTotalUpVotes()-1);
            upVoteRepository.deleteInBatch(Arrays.asList(upVote.get()));
        }

        PostDTO postDTO=new PostDTO();
        postDTO= Converters.convert(postDTO,post);

        postDTO.setPostUpVotedByCurrentUser(!upVote.isPresent());


        return new ResponseEntity<>(postDTO, HttpStatus.OK);
    }

}
