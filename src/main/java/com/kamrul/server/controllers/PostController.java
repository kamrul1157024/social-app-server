package com.kamrul.server.controllers;

import com.kamrul.server.dto.MedalDTO;
import com.kamrul.server.dto.PostDTO;
import com.kamrul.server.exception.ResourceNotFoundException;
import com.kamrul.server.exception.UnauthorizedException;
import com.kamrul.server.models.compositeKey.UserAndPostCompositeKey;
import com.kamrul.server.models.medal.MedalType;
import com.kamrul.server.models.post.Post;
import com.kamrul.server.models.user.User;
import com.kamrul.server.repositories.GeneralQueryRepository;
import com.kamrul.server.repositories.PostRepository;
import com.kamrul.server.repositories.MedalRepository;
import com.kamrul.server.repositories.UserRepository;
import com.kamrul.server.security.jwt.JWTUtil;
import com.kamrul.server.services.verify.Verifier;
import com.kamrul.server.services.verify.exception.VerificationException;
import com.kamrul.server.utils.Converters;
import com.kamrul.server.utils.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.*;

import static com.kamrul.server.utils.GeneralResponseMSG.*;

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
    private Verifier<PostDTO> postVerifier;


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

        if(jwtOptional.isEmpty() || !jwtOptional.get().startsWith("Bearer"))
            return new ResponseEntity<>(postDTO,HttpStatus.OK);

        Long loggedInUserId=JWTUtil.getUserIdFromJwt(jwtOptional.get());
        UserAndPostCompositeKey userAndPostCompositeKey =
                new UserAndPostCompositeKey(loggedInUserId,postId);

        Optional<MedalDTO> medal= medalRepository
                .findMedalByCompositeKey(userAndPostCompositeKey);

        MedalType medalGivenByLoggedInUser=
                medal.isPresent()? medal.get().getMedalType() : MedalType.NO_MEDAL;

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
            throws ResourceNotFoundException, UnauthorizedException, VerificationException,StackOverflowError {
        Long loggedInUserId= JWTUtil.getUserIdFromJwt(jwt);
        User user= GeneralQueryRepository.getByID(
                userRepository,
                loggedInUserId,
                USER_NOT_FOUND_MSG
        );
        postDTO.setUser(user);
        postVerifier.verify(postDTO);
        Post post=new Post();
        post=Converters.convert(postDTO,post);
        postRepository.save(post);
        return new ResponseEntity<>(post, HttpStatus.ACCEPTED);
    }

    @PutMapping
    @Transactional(rollbackOn = {Exception.class})
    public ResponseEntity<?> updatePost(@RequestBody PostDTO postDTO)
            throws ResourceNotFoundException, UnauthorizedException, VerificationException {

        User user= GeneralQueryRepository.getByID(
                userRepository,
                GeneralQueryRepository.getCurrentlyLoggedInUserId(),
                USER_NOT_FOUND_MSG
        );
        Post post= GeneralQueryRepository.getByID(
                postRepository,
                postDTO.getPostId(),
                POST_NOT_FOUND_MSG
        );
        if(!user.equals(post.getUser()))
            throw new UnauthorizedException("User Do no have permission to Update this post");
        postVerifier.verify(postDTO);
        post=Converters.convert(postDTO,post);
        postRepository.save(post);
        return new ResponseEntity<>(post,HttpStatus.OK);
    }


    @DeleteMapping
    public ResponseEntity<?> deletePost(@RequestParam(value = "id") Long postId)
            throws ResourceNotFoundException, UnauthorizedException {
        User user= GeneralQueryRepository.getByID(userRepository, GeneralQueryRepository.getCurrentlyLoggedInUserId(), USER_NOT_FOUND_MSG);
        Post post= GeneralQueryRepository.getByID(postRepository, postId, POST_NOT_FOUND_MSG);
        if(!user.equals(post.getUser()))
            throw new UnauthorizedException("User Do no have permission to delete this post");
        postRepository.deleteInBatch(Arrays.asList(post));
        return new ResponseEntity<>(new Message("Post Deleted Successfully"),HttpStatus.OK);
    }


}
