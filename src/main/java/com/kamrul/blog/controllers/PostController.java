package com.kamrul.blog.controllers;

import com.kamrul.blog.dto.MedalDTO;
import com.kamrul.blog.dto.PostDTO;
import com.kamrul.blog.exception.ResourceNotFoundException;
import com.kamrul.blog.exception.UnauthorizedException;
import com.kamrul.blog.models.compositeKey.UserAndPostCompositeKey;
import com.kamrul.blog.models.medal.MedalType;
import com.kamrul.blog.models.post.Post;
import com.kamrul.blog.models.user.User;
import com.kamrul.blog.repositories.GeneralQueryRepository;
import com.kamrul.blog.repositories.PostRepository;
import com.kamrul.blog.repositories.MedalRepository;
import com.kamrul.blog.repositories.UserRepository;
import com.kamrul.blog.security.jwt.JWTUtil;
import com.kamrul.blog.services.verify.Verifier;
import com.kamrul.blog.services.verify.exception.VerificationException;
import com.kamrul.blog.utils.Converters;
import com.kamrul.blog.utils.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
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
    @Autowired
    private Verifier<Post> postVerifier;


    @GetMapping
    public ResponseEntity<?> getPostById(
            @RequestParam(value = "id") Long postId ,
            @RequestHeader("Authorization") Optional<String> jwtOptional
    )
            throws ResourceNotFoundException, UnauthorizedException {

        Post post= GeneralQueryRepository.getByID(
                postRepository,
                postId,
                POST_NOT_FOUND_MSG
        );

        PostDTO postDTO=new PostDTO();
        postDTO= Converters.convert(post,postDTO);

        if(jwtOptional.isEmpty() || !jwtOptional.get().startsWith("Bearer")) return new ResponseEntity<>(postDTO,HttpStatus.OK);

        Long loggedInUserId=JWTUtil.getUserIdFromJwt(jwtOptional.get());
        UserAndPostCompositeKey userAndPostCompositeKey = new UserAndPostCompositeKey(loggedInUserId,postId);
        Optional<MedalDTO> medal= medalRepository.findMedalByCompositeKey(userAndPostCompositeKey);
        MedalType medalGivenByLoggedInUser= medal.isPresent()? medal.get().getMedalType() : MedalType.NO_MEDAL;
        postDTO.setMedalTypeProvidedByLoggedInUser(medalGivenByLoggedInUser);
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
    public ResponseEntity<?> getPostByPage(
            @PathVariable(value = "pageId") Integer pageId,
            @RequestHeader("Authorization") Optional<String> jwt) throws UnauthorizedException {

        final Integer pageSize=50;
        Page<Post> postDTOPage=postRepository.getTopPost(PageRequest.of(pageId-1,pageSize));
        List<PostDTO> postDTOs=new ArrayList<>();
        postDTOPage.forEach(post -> postDTOs.add(Converters.convert(post,new PostDTO())));

        if(jwt.isEmpty() || !jwt.get().startsWith("Bearer")) return new ResponseEntity<>(postDTOs,HttpStatus.OK);
        Long loggedInUserId=JWTUtil.getUserIdFromJwt(jwt.get());
        List<MedalDTO> medalGivenPostIds=medalRepository
                .getPostForCurrentlyLoggedInUserOnWhichUserGivenMedal(loggedInUserId);
        //<PostID, MedalType>
        HashMap<Long,MedalType> medalTypeGivenByUser=new HashMap<>();
        medalGivenPostIds.forEach(medalDTO->medalTypeGivenByUser.put(medalDTO.getPostId(), medalDTO.getMedalType()));
        postDTOs.forEach(postDTO->postDTO.setMedalTypeProvidedByLoggedInUser(medalTypeGivenByUser.getOrDefault(postDTO.getPostId(),MedalType.NO_MEDAL)));

        return new ResponseEntity<>(postDTOs,HttpStatus.OK);
    }

    @PostMapping
    @Transactional(rollbackOn = {Exception.class})
    public ResponseEntity<?> createPost(
            @RequestBody PostDTO postDTO,
            @RequestHeader("Authorization") String jwt)
            throws ResourceNotFoundException, UnauthorizedException, VerificationException {

        Long loggedInUserId= JWTUtil.getUserIdFromJwt(jwt);
        User user= GeneralQueryRepository.getByID(
                userRepository,
                loggedInUserId,
                USER_NOT_FOUND_MSG
        );
        postDTO.setUser(user);
        Post post=new Post();
        post=Converters.convert(postDTO,post);

        postVerifier.verify(post);

        postRepository.save(post);
        return new ResponseEntity<>(post, HttpStatus.ACCEPTED);
    }

    @PutMapping
    @Transactional(rollbackOn = {Exception.class})
    public ResponseEntity<?> updatePost(@RequestBody PostDTO postDetails)
            throws ResourceNotFoundException, UnauthorizedException, VerificationException {

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

        postVerifier.verify(post);

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
