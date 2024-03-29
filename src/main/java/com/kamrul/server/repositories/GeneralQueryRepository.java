package com.kamrul.server.repositories;

import com.kamrul.server.exception.ResourceNotFoundException;
import com.kamrul.server.exception.UnauthorizedException;
import com.kamrul.server.security.models.AppUserDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public abstract class GeneralQueryRepository {

    public static <T> T getByID(JpaRepository<? extends T, Long> repository,Long id,String message) throws ResourceNotFoundException {
        T t=repository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException(message));
        return t;
    }

    public static AppUserDetails getCurrentlyLoggedInUser() throws UnauthorizedException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            AppUserDetails userDetails = (AppUserDetails) authentication.getPrincipal();
            return userDetails;
        }
        throw new UnauthorizedException("Not logged In");
    }

    public static Long getCurrentlyLoggedInUserId() throws  UnauthorizedException
    {
        AppUserDetails appUserDetails= getCurrentlyLoggedInUser();
        return appUserDetails.getUserId();
    }
}
