package com.kamrul.blog.controllers;

import com.kamrul.blog.dto.MedalDTO;
import com.kamrul.blog.dto.PostDTO;
import com.kamrul.blog.exception.ResourceNotFoundException;
import com.kamrul.blog.exception.UnauthorizedException;
import com.kamrul.blog.models.MedalType;
import com.kamrul.blog.models.Post;
import com.kamrul.blog.models.Medal;
import com.kamrul.blog.models.User;
import com.kamrul.blog.repositories.GeneralQueryRepository;
import com.kamrul.blog.repositories.PostRepository;
import com.kamrul.blog.repositories.MedalRepository;
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

import static com.kamrul.blog.utils.GeneralResponseMSG.*;

@CrossOrigin
@RestController
@RequestMapping("/api/post")
public class PostController {
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MedalRepository medalRepository;

    @GetMapping
    public ResponseEntity<?> getPostById(@RequestParam(value = "id") Long postId)
            throws ResourceNotFoundException {

        Post post= GeneralQueryRepository.getByID(
                postRepository,
                postId,
                POST_NOT_FOUND_MSG
                );


        PostDTO postDTO=new PostDTO();
        postDTO= Converters.convert(post,postDTO);

        try {
            User loggedInUser= GeneralQueryRepository.getByID(
                    userRepository,
                    GeneralQueryRepository.getCurrentlyLoggedInUserId(),
                    USER_NOT_FOUND_MSG
            );

            Optional<MedalDTO> medal= medalRepository.findMedalByUserIdAndPostID(loggedInUser.getUserId(),post.getPostId());
            MedalType medalGivenByLoggedInUser= medal.isPresent()? medal.get().getMedalType() : MedalType.NO_MEDAL;
            postDTO.setMedalTypeProvidedByLoggedInUser(medalGivenByLoggedInUser);

        } catch (UnauthorizedException unauthorizedException) {
            System.out.println("Anonymous User");
        }

        return new ResponseEntity<>(postDTO,HttpStatus.OK);
    }

    @GetMapping("medalGivers")
    public ResponseEntity<?> getMedalGiversOfThePost(@RequestParam(value = "id")Long postId) throws ResourceNotFoundException {
        Post post= GeneralQueryRepository.getByID(
                postRepository,
                postId,
                POST_NOT_FOUND_MSG
        );
        return new ResponseEntity<>(post.getMedals(),HttpStatus.OK);
    }

    @GetMapping("/page/{pageId}")
    public ResponseEntity<?> getPostByPage(@PathVariable(value = "pageId") Integer pageId)
    {
        final Integer pageSize=10;
        Page<Post> postDTOPage=postRepository.getTopPost(PageRequest.of(pageId-1,pageSize));
        List<PostDTO> postDTOs=new ArrayList<>();
        postDTOPage.forEach(post -> postDTOs.add(Converters.convert(post,new PostDTO())));
        try {
            Long loggedInUserId=GeneralQueryRepository.getCurrentlyLoggedInUserId();
            List<MedalDTO> medalGivenPostIds=postRepository
                    .getPostForCurrentlyLoggedInUserOnWhichUserGivenMedal(loggedInUserId);

            //<PostID, MedalType>
            HashMap<Long,MedalType> medalTypeGivenByUser=new HashMap<>();
            medalGivenPostIds.forEach(medalDTO->medalTypeGivenByUser.put(medalDTO.getPostId(), medalDTO.getMedalType()));
            postDTOs.forEach(postDTO->postDTO.setMedalTypeProvidedByLoggedInUser(medalTypeGivenByUser.getOrDefault(postDTO.getPostId(),MedalType.NO_MEDAL)));

        } catch (UnauthorizedException unauthorizedException) {
            System.out.println("UnAuthorised User");
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
        Post post=new Post();
        post=Converters.convert(postDTO,post);
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

        post=Converters.convert(postDetails,post);

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
