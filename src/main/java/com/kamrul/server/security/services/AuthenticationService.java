package com.kamrul.server.security.services;

import com.kamrul.server.dto.UserDTO;
import com.kamrul.server.exception.UnauthorizedException;
import com.kamrul.server.models.user.User;
import com.kamrul.server.repositories.UserRepository;
import com.kamrul.server.security.jwt.JWTUtil;
import com.kamrul.server.security.models.AppUserDetails;
import com.kamrul.server.security.models.AuthenticationRequest;
import com.kamrul.server.security.models.AuthenticationResponse;
import com.kamrul.server.utils.Converters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
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
        System.out.println(userDTO);
        User user=new User();
        user= Converters.convert(userDTO,user);
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        userRepository.save(user);
        UserDTO userOutDTO= Converters.convert(user);
        return new ResponseEntity<>(userOutDTO,HttpStatus.ACCEPTED);
    }

    @GetMapping("/test")
    public String user()
    {
        return "<h1>test Successful</h1>";
    }



}
