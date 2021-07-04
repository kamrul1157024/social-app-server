package com.kamrul.server.security.filters;


import com.kamrul.server.exception.UnauthorizedException;
import com.kamrul.server.security.jwt.JWTUtil;
import com.kamrul.server.security.models.AppUserDetails;
import com.kamrul.server.security.services.AppUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JWTRequestFilter extends OncePerRequestFilter {

    @Autowired
    private JWTUtil jwtUtil;
    @Autowired
    AppUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException
    {
        final String authorizationHeader=request.getHeader("Authorization");
        Long userId=null;
        String jwt;
        if (authorizationHeader!=null && authorizationHeader.startsWith("Bearer "))
        {
            jwt=authorizationHeader.substring(7);
            try {
                userId=jwtUtil.extractUserId(jwt);
            } catch (UnauthorizedException unauthorizedException) {
                unauthorizedException.printStackTrace();
            }
        }


        if(userId!=null && SecurityContextHolder.getContext().getAuthentication()==null)
        {
            AppUserDetails userDetails=userDetailsService.loadUserByUserId(userId);
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken
                    =new UsernamePasswordAuthenticationToken(
                    userDetails,null,userDetails.getAuthorities()
            );

            usernamePasswordAuthenticationToken
                    .setDetails(
                            new WebAuthenticationDetailsSource()
                                    .buildDetails(request)
                    );

            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        }

        filterChain.doFilter(request,response);
    }
}
