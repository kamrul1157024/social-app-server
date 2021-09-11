package com.kamrul.server.controllers;

import com.kamrul.server.dto.MedalDTO;
import com.kamrul.server.dto.PostDTO;
import com.kamrul.server.exception.ResourceNotFoundException;
import com.kamrul.server.exception.UnauthorizedException;
import com.kamrul.server.models.compositeKey.UserAndPostCompositeKey;
import com.kamrul.server.models.medal.Medal;
import com.kamrul.server.models.medal.MedalType;
import com.kamrul.server.models.post.Post;
import com.kamrul.server.models.user.User;
import com.kamrul.server.repositories.GeneralQueryRepository;
import com.kamrul.server.repositories.PostRepository;
import com.kamrul.server.repositories.MedalRepository;
import com.kamrul.server.repositories.UserRepository;
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
import java.util.stream.Collectors;

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


    @GetMapping("/{id}")
    public ResponseEntity<?> getPostById(@PathVariable(value = "id") Long postId ,@RequestAttribute("userId") Long loggedInUserId)
            throws ResourceNotFoundException {
        Post post= GeneralQueryRepository.getByID(postRepository, postId, POST_NOT_FOUND);
        if (post.getDraft() && (loggedInUserId==null || post.getUser().getUserId()!=loggedInUserId))
            throw new ResourceNotFoundException(POST_NOT_FOUND);
        PostDTO postDTO= Converters.convert(post);
        if(loggedInUserId==null)
            return new ResponseEntity<>(postDTO,HttpStatus.OK);
        UserAndPostCompositeKey userAndPostCompositeKey =  new UserAndPostCompositeKey(loggedInUserId,postId);
        Optional<Medal> medal= medalRepository.findMedalByUserAndPostCompositeKey(userAndPostCompositeKey);
        MedalType medalGivenByLoggedInUser= medal.isPresent()? medal.get().getMedalType() : MedalType.NO_MEDAL;
        postDTO.setMedalTypeProvidedByLoggedInUser(medalGivenByLoggedInUser);
        return new ResponseEntity<>(postDTO,HttpStatus.OK);
    }

    @GetMapping("/{id}/medalGivers")
    public ResponseEntity<?> getMedalGiversOfThePost(@PathVariable(value = "id")Long postId){
        List<Medal> medals = medalRepository.findMedalsByPostId(postId);
        List<MedalDTO> medalDTOS = medals.stream().map(medal->new MedalDTO(medal)).collect(Collectors.toList());
        return new ResponseEntity<>(medalDTOS,HttpStatus.OK);
    }

    @GetMapping("/page/{pageId}")
    public ResponseEntity<?> getPostByPage(@PathVariable(value = "pageId") Integer pageId, @RequestAttribute("userId") Long loggedInUserId) {
        final Integer pageSize=500;
        Page<Post> postDTOPage=postRepository.getTopPost(PageRequest.of(pageId-1,pageSize));
        List<PostDTO> postDTOs=new ArrayList<>();
        postDTOPage.forEach(post -> postDTOs.add(Converters.convert(post)));
        if(loggedInUserId==null) return new ResponseEntity<>(postDTOs,HttpStatus.OK);

        List<Medal> medalGivenPostIds=medalRepository.findMedalsByUserId(loggedInUserId);
        //<PostID, MedalType>
        HashMap<Long,MedalType> medalTypeGivenByUser=new HashMap<>();
        medalGivenPostIds.forEach(medalDTO->medalTypeGivenByUser.put(medalDTO.getPost().getPostId(), medalDTO.getMedalType()));
        postDTOs.forEach(postDTO->postDTO.setMedalTypeProvidedByLoggedInUser(medalTypeGivenByUser.getOrDefault(postDTO.getPostId(),MedalType.NO_MEDAL)));
        return new ResponseEntity<>(postDTOs,HttpStatus.OK);
    }

    @PostMapping
    @Transactional(rollbackOn = {Exception.class})
    public ResponseEntity<?> createPost(@RequestBody PostDTO postDTO,@RequestAttribute("userId") Long loggedInUserId)
            throws ResourceNotFoundException, VerificationException,StackOverflowError {
        User user= GeneralQueryRepository.getByID(userRepository,loggedInUserId, USER_NOT_FOUND);
        postDTO.setUser(user);
        postVerifier.verify(postDTO);
        Post post=new Post();
        post=Converters.convert(postDTO,post);
        post = postRepository.save(post);
        return new ResponseEntity<>(post, HttpStatus.ACCEPTED);
    }

    @PutMapping
    @Transactional(rollbackOn = {Exception.class})
    public ResponseEntity<?> updatePost(@RequestBody PostDTO postDTO, @RequestAttribute("userId") Long loggedInUserId)
            throws ResourceNotFoundException, UnauthorizedException, VerificationException {
        User user= GeneralQueryRepository.getByID(userRepository,loggedInUserId, USER_NOT_FOUND);
        Post post= GeneralQueryRepository.getByID(postRepository, postDTO.getPostId(), POST_NOT_FOUND);
        if(!user.equals(post.getUser()))
            throw new UnauthorizedException("User Do not have permission to Update this post");
        postVerifier.verify(postDTO);
        post=Converters.convert(postDTO,post);
        post = postRepository.save(post);
        PostDTO responsePostDto = Converters.convert(post);
        return new ResponseEntity<>(responsePostDto,HttpStatus.OK);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePost(@PathVariable(value = "id") Long postId,@RequestAttribute("userId") Long loggedInUserId)
            throws ResourceNotFoundException, UnauthorizedException {
        User user= GeneralQueryRepository.getByID(userRepository, loggedInUserId, USER_NOT_FOUND);
        Post post= GeneralQueryRepository.getByID(postRepository, postId, POST_NOT_FOUND);
        if(!user.equals(post.getUser()))
            throw new UnauthorizedException("User Do no have permission to delete this post");
        postRepository.deleteInBatch(Arrays.asList(post));
        return new ResponseEntity<>(new Message("Post Deleted Successfully"),HttpStatus.OK);
    }
}
