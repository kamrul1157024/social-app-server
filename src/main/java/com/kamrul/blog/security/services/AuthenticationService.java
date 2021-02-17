package com.kamrul.blog.security.services;

import com.kamrul.blog.exception.UnauthorizedException;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthenticationService {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private AppUserDetailsService userDetailsService;
    @Autowired
    private JWTUtil jwtUtil;

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

    @GetMapping("/test")
    public String user()
    {
        return "<h1>test Successful</h1>";
    }



}
