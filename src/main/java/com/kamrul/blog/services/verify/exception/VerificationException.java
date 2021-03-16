package com.kamrul.blog.services.verify.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public  class VerificationException extends Exception {


    public VerificationException() {
        super();
    }

    public VerificationException(String message) {
        super(message);
    }
}
