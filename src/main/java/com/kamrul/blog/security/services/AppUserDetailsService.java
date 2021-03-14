package com.kamrul.blog.security.services;

import com.kamrul.blog.models.user.User;
import com.kamrul.blog.repositories.UserRepository;
import com.kamrul.blog.security.models.AppUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class AppUserDetailsService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Override
    public AppUserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        Optional<User> user=userRepository.findByUserName(userName);
        user.orElseThrow(
                ()->new UsernameNotFoundException("No user with : "+userName)
                );
        return user.map(AppUserDetails::new).get();
    }

    public AppUserDetails loadUserByUserId(Long userId) throws UsernameNotFoundException
    {
        Optional<User> user=userRepository.findById(userId);
        user.orElseThrow(
                ()->new UsernameNotFoundException("Not a valid user")
        );
        return user.map(AppUserDetails::new).get() ;
    }

}
