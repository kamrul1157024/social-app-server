package com.kamrul.blog.controllers;

import com.kamrul.blog.dto.PostDTO;
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
import com.kamrul.blog.utils.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import static com.kamrul.blog.utils.GeneralResponseMessages.*;

@CrossOrigin
@RestController
@RequestMapping("/api/post")
public class PostController {
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UpVoteRepository upVoteRepository;

    @GetMapping
    public ResponseEntity<?> getPostById(@RequestParam(value = "id") Long postId)
            throws ResourceNotFoundException {
        Post post= GeneralQueryRepository.getByID(
                postRepository,
                postId,
                POST_NOT_FOUND_MSG
                );

        Boolean isUserLoggedIn=false;

        Boolean isUserUpVotedCurrentPost=false;

        PostDTO postDTO=new PostDTO();
        postDTO= Converters.convert(postDTO,post);

        try {
            User user= GeneralQueryRepository.getByID(
                    userRepository,
                    GeneralQueryRepository.getCurrentlyLoggedInUserId(),
                    USER_NOT_FOUND_MSG
            );
            isUserLoggedIn=true;

            Optional<UpVote> upVote=upVoteRepository.
                    findUpVoteByUserIdAndPostID(user.getUserId(),post.getPostId());

            if(upVote.isPresent()) isUserUpVotedCurrentPost=true;
            postDTO.setPostUpVotedByCurrentUser(isUserLoggedIn && isUserUpVotedCurrentPost);
        } catch (UnauthorizedException unauthorizedException) {
            System.out.println("Anonymous User");
        }

        return new ResponseEntity<>(postDTO,HttpStatus.OK);
    }

    @GetMapping("upvoters")
    public ResponseEntity<?> getUpVotersOfThePost(@RequestParam(value = "id")Long postId) throws ResourceNotFoundException {
        Post post= GeneralQueryRepository.getByID(
                postRepository,
                postId,
                POST_NOT_FOUND_MSG
        );


        return new ResponseEntity<>(post.getUpVotes(),HttpStatus.OK);
    }

    @GetMapping("/page/{pageId}")
    public ResponseEntity<?> getPostByPage(@PathVariable(value = "pageId") Integer pageId)
    {
        final Integer pageSize=10;

        Page<Post> posts=postRepository.getTopPost(PageRequest.of(pageId-1,pageSize));

        List<PostDTO> postDTOs=new ArrayList<>();

        posts.forEach(post -> {
            PostDTO postDTO=new PostDTO();
            postDTO=Converters.convert(postDTO,post);
            postDTO.setComments(null);
            postDTOs.add(postDTO);
        });


        try {

            Long loggedInUserId=GeneralQueryRepository.getCurrentlyLoggedInUserId();
            List<Long> upVotedPostIds=postRepository
                    .getPostWhichIsUpVotedByCurrentlyLoggedInUser(loggedInUserId);

            HashMap<Long,Boolean> isUpVoted=new HashMap<>();
            upVotedPostIds.forEach((postId)->isUpVoted.put(postId,true));

            postDTOs.forEach(postDTO -> postDTO.setPostUpVotedByCurrentUser(
                    isUpVoted.getOrDefault(postDTO.getPostId(),false)
                    )
            );

        } catch (UnauthorizedException unauthorizedException) {

            //Serving to Unauthorized User

        }

        return new ResponseEntity<>(postDTOs,HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> createPost(@RequestBody PostDTO postDTO)
            throws ResourceNotFoundException, UnauthorizedException {
        User user= GeneralQueryRepository.getByID(
                userRepository,
                GeneralQueryRepository.getCurrentlyLoggedInUserId(),
                USER_NOT_FOUND_MSG
        );
        postDTO.setUser(user);

        System.out.println(postDTO);

        Post post=new Post();
        post=Converters.convert(post,postDTO);

        System.out.println(post);

        postRepository.save(post);

        return new ResponseEntity<>(post, HttpStatus.ACCEPTED);
    }

    @PutMapping
    public ResponseEntity<?> updatePost(@RequestBody PostDTO postDetails) throws ResourceNotFoundException, UnauthorizedException {

        User user= GeneralQueryRepository.getByID(
                userRepository,
                GeneralQueryRepository.getCurrentlyLoggedInUserId(),
                USER_NOT_FOUND_MSG
        );

        Post post= GeneralQueryRepository.getByID(
                postRepository,
                postDetails.getPostId(),
                POST_NOT_FOUND_MSG
        );

        if(!user.equals(post.getUser()))
            throw new UnauthorizedException("User Do no have permission to Update this post");

        post=Converters.convert(post,postDetails);

        postRepository.save(post);

        return new ResponseEntity<>(post,HttpStatus.OK);
    }


    @DeleteMapping
    public ResponseEntity<?> deletePost(@RequestParam(value = "id") Long postId)
            throws ResourceNotFoundException, UnauthorizedException {
        User user= GeneralQueryRepository.getByID(
                userRepository,
                GeneralQueryRepository.getCurrentlyLoggedInUserId(),
                USER_NOT_FOUND_MSG
        );
        Post post= GeneralQueryRepository.getByID(
                postRepository,
                postId,
                POST_NOT_FOUND_MSG
        );

        if(!user.equals(post.getUser()))
            throw new UnauthorizedException("User Do no have permission to delete this post");

        postRepository.deleteInBatch(Arrays.asList(post));

        return new ResponseEntity<>(new Message("Post Deleted Successfully"),HttpStatus.OK);
    }


}
