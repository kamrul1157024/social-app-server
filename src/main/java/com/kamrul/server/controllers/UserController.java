package com.kamrul.server.controllers;

import com.kamrul.server.dto.PostDTO;
import com.kamrul.server.dto.UserDTO;
import com.kamrul.server.exception.ResourceNotFoundException;
import com.kamrul.server.models.booklet.Booklet;
import com.kamrul.server.models.post.Post;
import com.kamrul.server.models.user.User;
import com.kamrul.server.repositories.*;
import com.kamrul.server.utils.Converters;
import com.kamrul.server.utils.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;
import java.util.stream.Collectors;

import static com.kamrul.server.utils.GeneralResponseMSG.USER_NOT_FOUND;

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
        return (user.getDeleted()==null || user.getDeleted()==true);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> getUserDetails(@PathVariable(value = "userId") Long userId)
            throws ResourceNotFoundException {
        User user= GeneralQueryRepository.getByID(userRepository,userId, USER_NOT_FOUND);
        if(this.isUserDeleted(user)) throw new ResourceNotFoundException(USER_NOT_FOUND);
        UserDTO userDTO=Converters.convert(user);
        return new ResponseEntity<>(userDTO,HttpStatus.OK);
    }

    @GetMapping("/userName/{userName}")
    public ResponseEntity<?> getUserDetailsByUserName(@PathVariable("userName") String userName)
            throws ResourceNotFoundException {
        Optional<User> user=userRepository.findByUserName(userName);
        if(user.isEmpty() || (!user.isEmpty() && this.isUserDeleted(user.get()))) throw new ResourceNotFoundException(USER_NOT_FOUND);
        UserDTO userDTO=Converters.convert(user.get());
        return new ResponseEntity<>(userDTO,HttpStatus.OK);
    }

    @GetMapping("/currentlyLoggedInUser")
    public ResponseEntity<?> getCurrentlyLoggedInUser(@RequestAttribute("userId")Long userId) throws ResourceNotFoundException {
        User user= GeneralQueryRepository.getByID(userRepository, userId, USER_NOT_FOUND);
        return new ResponseEntity<>(user,HttpStatus.OK);
    }

    @GetMapping("/{userId}/posts")
    public ResponseEntity<?> getUserPost(
            @PathVariable(value = "userId") Long userId,
            @RequestParam("pageNo") Integer pageNo,
            @RequestAttribute("userId") Long currentlyLoggedInUserId){
        final Integer pageSize=500;
        if(currentlyLoggedInUserId==null){
            Page<Post> postPage=postRepository.getPostByUserId(userId, PageRequest.of(pageNo-1,pageSize));
            List<PostDTO> postDTOs = postPage
                    .stream()
                    .map(post -> new PostDTO(post)).collect(Collectors.toList());
            return new ResponseEntity<>(postDTOs,HttpStatus.OK);
        }
        Page<PostDTO> postDTOs = postRepository
                .getPostsByUserIdWithMedalsGivenByLoggedInUser(
                        userId,
                        currentlyLoggedInUserId,
                        PageRequest.of(pageNo-1,pageSize)
                );
        return new ResponseEntity<>(postDTOs.toList(),HttpStatus.OK);
    }

    @GetMapping("/booklet")
    ResponseEntity<?> getUserBooklet(@RequestParam("userId")Long userId) throws ResourceNotFoundException {
        /*Just To through ResourceNotFoundException*/
        GeneralQueryRepository.getByID(userRepository, userId, USER_NOT_FOUND);
        List<Booklet> userBookLets=bookletRepository.getBookletByUserId(userId);
        return new ResponseEntity<>(userBookLets,HttpStatus.OK);
    }

    @GetMapping("/getSavedPosts")
    ResponseEntity<?> getSavedPostByLoggedInUser(@RequestAttribute("userId")Long userId){
        List<Post> savedPost=savedPostRepository.getSavedPostByUserId(userId);
        return new ResponseEntity<>(savedPost,HttpStatus.OK);
    }


    @PutMapping
    public ResponseEntity<?> updateUser(@RequestBody UserDTO userDTO,@RequestAttribute("userId") Long userId)
            throws ResourceNotFoundException {
        User user= GeneralQueryRepository.getByID(userRepository, userId, USER_NOT_FOUND);
        user=Converters.convert(userDTO,user);
        user = userRepository.save(user);
        return new ResponseEntity<>(user,HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteUser(@RequestAttribute("userId") Long userId)
            throws ResourceNotFoundException {
        User user= GeneralQueryRepository.getByID(userRepository,userId, USER_NOT_FOUND);
        user.setDeleted(true);
        userRepository.save(user);
        return new ResponseEntity<>(new Message("User deleted"),HttpStatus.OK);
    }
}
