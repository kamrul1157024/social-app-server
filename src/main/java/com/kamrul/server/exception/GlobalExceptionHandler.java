package com.kamrul.server.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> resourceNotFoundExceptionHandler(ResourceNotFoundException resourceNotFoundException, WebRequest webRequest)
    {
        ErrorDetails errorDetails=new ErrorDetails(
                new Date(),
                resourceNotFoundException.getMessage(),
                "Please Check The Provided Information"
        );
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<?> unauthorizedException(UnauthorizedException unauthorizedException,WebRequest webRequest)
    {
        ErrorDetails errorDetails=new ErrorDetails(
                new Date(),
                unauthorizedException.getMessage(),
                "You are not authorized to do this"
        );
        return new ResponseEntity<>(errorDetails,HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> globalExceptionHandler(Exception e,WebRequest webRequest)
    {
        ErrorDetails errorDetails=new ErrorDetails(
          new Date(),
          "something is wrong!",
                "Please Check The Documentation"
        );
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);

    }
}
