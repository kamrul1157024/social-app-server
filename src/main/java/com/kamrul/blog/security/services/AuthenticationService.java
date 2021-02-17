package com.kamrul.blog.security.services;

import com.kamrul.blog.dto.UserDTO;
import com.kamrul.blog.exception.UnauthorizedException;
import com.kamrul.blog.models.User;
import com.kamrul.blog.repositories.UserRepository;
import com.kamrul.blog.security.jwt.JWTUtil;
import com.kamrul.blog.security.models.AppUserDetails;
import com.kamrul.blog.security.models.AuthenticationRequest;
import com.kamrul.blog.security.models.AuthenticationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthenticationService {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private AppUserDetailsService userDetailsService;
    @Autowired
    private JWTUtil jwtUtil;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @PostMapping("/authenticate")
    public ResponseEntity<?> performAuthentication(@RequestBody AuthenticationRequest authenticationRequest) throws UnauthorizedException {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authenticationRequest.getUserName(),
                            authenticationRequest.getPassword()
                    )
            );
        }catch (BadCredentialsException badCredentialsException)
        {
            throw new UnauthorizedException("Incorrect UserName Or password");
        }

        final AppUserDetails userDetails= userDetailsService
                .loadUserByUsername(
                        authenticationRequest.getUserName()
                );
        final String jwt=jwtUtil.generateToken(userDetails);
        return new ResponseEntity<>(new AuthenticationResponse(jwt), HttpStatus.OK);

    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserDTO userDTO)
    {
        User user=new User();
        user.setUserName(userDTO.getUsername());
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setEmail(userDTO.getEmail());
        user.setDateOfBirth(userDTO.getDateOfBirth());

        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));

        userRepository.save(user);

        UserDTO userOutDTO=new UserDTO();
        userOutDTO.setUsername(user.getUserName());
        userOutDTO.setEmail(user.getEmail());
        userOutDTO.setFirstName(user.getFirstName());
        userOutDTO.setLastName(user.getLastName());
        userOutDTO.setIsEmailVerified(userOutDTO.getEmailVerified());
        userOutDTO.setDateOfBirth(user.getDateOfBirth());


        return new ResponseEntity<>(userOutDTO,HttpStatus.ACCEPTED);
    }

    @GetMapping("/test")
    public String user()
    {
        return "<h1>test Successful</h1>";
    }



}
