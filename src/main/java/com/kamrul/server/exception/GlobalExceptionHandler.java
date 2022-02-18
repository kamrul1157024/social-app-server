
package com.kamrul.server.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> resourceNotFoundExceptionHandler(ResourceNotFoundException resourceNotFoundException, WebRequest webRequest)
    {
        ErrorDetails errorDetails=new ErrorDetails(
                new Date(),
                resourceNotFoundException.getMessage(),
                "Please Check The Provided Information"
        );
        log.warn(
                "Resource Not Found Exception  requestInfo {} message {}",
                webRequest.getDescription(true),
                resourceNotFoundException.getMessage()
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
        log.info(
                "Unauthorized request  requestInfo {}",
                webRequest.getDescription(true),
                unauthorizedException.getMessage()
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
        log.error(
                "Error Occurred while processing this request  requestInfo {}",
                webRequest.getDescription(true),
                e
        );
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);

    }
}