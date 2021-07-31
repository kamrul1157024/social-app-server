package com.kamrul.server.controllers;

import com.kamrul.server.dto.MedalDTO;
import com.kamrul.server.dto.PostDTO;
import com.kamrul.server.dto.UserDTO;
import com.kamrul.server.exception.ResourceNotFoundException;
import com.kamrul.server.exception.UnauthorizedException;
import com.kamrul.server.models.booklet.Booklet;
import com.kamrul.server.models.medal.MedalType;
import com.kamrul.server.models.post.Post;
import com.kamrul.server.models.user.User;
import com.kamrul.server.repositories.*;
import com.kamrul.server.security.jwt.JWTUtil;
import com.kamrul.server.utils.Converters;
import com.kamrul.server.utils.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;
import static com.kamrul.server.utils.GeneralResponseMSG.USER_NOT_FOUND_MSG;

@CrossOrigin
@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private MedalRepository medalRepository;
    @Autowired
    private BookletRepository bookletRepository;
    @Autowired
    private SavedPostRepository savedPostRepository;

    private Boolean isUserDeleted(User user){
        System.out.println(user);
        return (user.getDeleted()==null || user.getDeleted()==true);
    }

    @GetMapping("/details")
    public ResponseEntity<?> getUserDetails(@RequestParam(value = "id") Long userId)
            throws ResourceNotFoundException {
        User user= GeneralQueryRepository.getByID(userRepository,userId,USER_NOT_FOUND_MSG);
        if(this.isUserDeleted(user)) throw new ResourceNotFoundException(USER_NOT_FOUND_MSG);
        UserDTO userDTO=new UserDTO();
        userDTO=Converters.convert(user,userDTO);
        return new ResponseEntity<>(userDTO,HttpStatus.OK);
    }

    @GetMapping("/{userName}")
    public ResponseEntity<?> getUserDetailsByUserName(@PathVariable("userName") String userName)
            throws ResourceNotFoundException {
        Optional<User> user=userRepository.findByUserName(userName);
        if(user.isEmpty() || (!user.isEmpty() && this.isUserDeleted(user.get()))) throw new ResourceNotFoundException(USER_NOT_FOUND_MSG);
        UserDTO userDTO=Converters.convert(user.get(),new UserDTO());
        return new ResponseEntity<>(userDTO,HttpStatus.OK);
    }

    @GetMapping("/currentlyLoggedInUser")
    public ResponseEntity<?> getCurrentlyLoggedInUser(@RequestHeader("Authorization") String jwt)
            throws ResourceNotFoundException, UnauthorizedException {
        Long userId = JWTUtil.getUserIdFromJwt(jwt);
        User user= GeneralQueryRepository.getByID(
                userRepository,
                userId,
                USER_NOT_FOUND_MSG);
        return new ResponseEntity<>(user,HttpStatus.OK);
    }

    @GetMapping("/posts")
    public ResponseEntity<?> getUserPost(
            @RequestParam(value = "userId") Long userId,
            @RequestParam("pageNo") Integer pageNo,
            @RequestHeader("Authorization") Optional<String> jwtOptional
    ) throws UnauthorizedException {

        final Integer pageSize=40;
        Page<Post> postDTOPage=postRepository.getPostByUserId(userId, PageRequest.of(pageNo-1,pageSize));
        List<PostDTO> postDTOS = new ArrayList<>();
        postDTOPage.forEach(post -> postDTOS.add(Converters.convert(post,new PostDTO())));

        if(jwtOptional.isEmpty() || !jwtOptional.get().startsWith("Bearer")) return new ResponseEntity<>(postDTOS,HttpStatus.OK);
        final String jwt=jwtOptional.get();

        Long currentlyLoggedInUserId
                =JWTUtil.getUserIdFromJwt(jwt);

        List<MedalDTO> medalGivenPostOfCurrentlyLoggedInUser= medalRepository
                .getPostIdForUserIdOnWhichCurrentlyLoggedInUserGivenMedal(
                        currentlyLoggedInUserId,
                        userId
                );

        /*<PostID,NumberOfBahOnPostGivenByUser>*/
        HashMap<Long, MedalType> medalTypeGivenByLoggedInUser=new HashMap<>();
        medalGivenPostOfCurrentlyLoggedInUser.forEach((medalDTO)->
                medalTypeGivenByLoggedInUser
                        .put(
                                medalDTO.getPostId(),
                                medalDTO.getMedalType()
                        )
        );

        postDTOS.forEach(postDTO ->
                postDTO.setMedalTypeProvidedByLoggedInUser(
                        medalTypeGivenByLoggedInUser
                                .getOrDefault(postDTO.getPostId(),MedalType.NO_MEDAL)
                )
        );
        return new ResponseEntity<>(postDTOS,HttpStatus.OK);
    }


    @GetMapping("/booklet")
    ResponseEntity<?> getUserBooklet(@RequestParam("userId")Long userId)
            throws ResourceNotFoundException {
        /*Just To through ResourceNotFoundException*/
        GeneralQueryRepository.getByID(
                userRepository,
                userId,
                USER_NOT_FOUND_MSG
        );
        List<Booklet> userBookLets=bookletRepository.getBookletByUserId(userId);
        return new ResponseEntity<>(userBookLets,HttpStatus.OK);
    }


    @GetMapping("/getSavedPosts")
    ResponseEntity<?> getSavedPostByLoggedInUser(@RequestHeader("Authorization") Optional<String> jwt) throws UnauthorizedException {
        if(jwt.isEmpty()) new UnauthorizedException("LogIn first!");
        Long userId=JWTUtil.getUserIdFromJwt(jwt.get());
        List<Post> savedPost=savedPostRepository.getSavedPostByUserId(userId);
        return new ResponseEntity<>(savedPost,HttpStatus.OK);
    }


    @PutMapping
    public ResponseEntity<?> updateUser(@RequestBody UserDTO userDTO)
            throws ResourceNotFoundException, UnauthorizedException {

        User user= GeneralQueryRepository.getByID(
                userRepository,
                GeneralQueryRepository.getCurrentlyLoggedInUserId(),
                USER_NOT_FOUND_MSG
        );

        user=Converters.convert(userDTO,user);
        userRepository.save(user);

        return new ResponseEntity<>(user,HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteUser(@RequestHeader("Authorization") Optional<String> jwt)
            throws ResourceNotFoundException, UnauthorizedException {
        if(jwt.isEmpty()) throw new UnauthorizedException(USER_NOT_FOUND_MSG);
        User user= GeneralQueryRepository.getByID(
                userRepository,
                GeneralQueryRepository.getCurrentlyLoggedInUserId(),
                USER_NOT_FOUND_MSG);
        user.setDeleted(true);
        userRepository.save(user);
        return new ResponseEntity<>(new Message("User deleted"),HttpStatus.OK);
    }


}
