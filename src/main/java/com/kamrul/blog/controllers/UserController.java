package com.kamrul.blog.controllers;

import com.kamrul.blog.dto.UserDTO;
import com.kamrul.blog.exception.ResourceNotFoundException;
import com.kamrul.blog.exception.UnauthorizedException;
import com.kamrul.blog.models.Post;
import com.kamrul.blog.models.User;
import com.kamrul.blog.repositories.GeneralQuery;
import com.kamrul.blog.repositories.UserRepository;
import com.kamrul.blog.utils.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

import static com.kamrul.blog.utils.RESPONSE_MSG.USER_NOT_FOUND_MSG;

@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/details")
    public ResponseEntity<?> getUserDetails(@RequestParam(value = "id") Long userId)
            throws ResourceNotFoundException {

        User user= GeneralQuery.getByID(userRepository,userId,USER_NOT_FOUND_MSG);

        UserDTO userDTO=new UserDTO();
        userDTO.setEmail(user.getEmail());
        userDTO.setFirstName(user.getFirstName());
        userDTO.setLastName(user.getLastName());
        userDTO.setUsername(user.getUserName());
        userDTO.setDateOfBirth(user.getDateOfBirth());
        userDTO.setIsEmailVerified(user.getEmailVerified());

        return new ResponseEntity<>(userDTO,HttpStatus.OK);
    }

    @GetMapping("/posts")
    public ResponseEntity<?> getUserPost(@RequestParam(value = "id") Long userId)
            throws ResourceNotFoundException {
        User user= GeneralQuery.getByID(userRepository,userId,USER_NOT_FOUND_MSG);
        List<Post> posts=user.getPosts();
        return new ResponseEntity<>(posts,HttpStatus.OK);
    }


    @PutMapping
    public ResponseEntity<?> updateUser(@RequestBody UserDTO userDTO)
            throws ResourceNotFoundException, UnauthorizedException {

        User user= GeneralQuery.getByID(
                userRepository,
                GeneralQuery.getCurrentlyLoggedInUserId(),
                USER_NOT_FOUND_MSG
        );

        user.setUserName(userDTO.getUsername());
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setEmail(userDTO.getEmail());
        user.setPassword(userDTO.getPassword());


        userRepository.save(user);
        return new ResponseEntity<>(user,HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteUser()
            throws ResourceNotFoundException, UnauthorizedException {

        User user= GeneralQuery.getByID(
                userRepository,
                GeneralQuery.getCurrentlyLoggedInUserId(),
                USER_NOT_FOUND_MSG);

        userRepository.deleteInBatch(Arrays.asList(user));
        return new ResponseEntity<>(new Message("User deleted"),HttpStatus.OK);
    }


}
