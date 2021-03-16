package com.kamrul.blog.services.verify.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS)
public class PoliticalPostException extends VerificationException {

    public PoliticalPostException() {
        super("Political posts are not allowed!");
    }
}
