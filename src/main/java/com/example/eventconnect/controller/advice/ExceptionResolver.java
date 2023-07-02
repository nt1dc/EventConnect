package com.example.eventconnect.controller.advice;

import com.example.eventconnect.exception.EventContractNotFoundException;
import com.example.eventconnect.exception.EventNotFoundException;
import com.example.eventconnect.exception.UserAlreadyExistsException;
import com.example.eventconnect.exception.UserNotFoundException;
import com.example.eventconnect.model.dto.Error;
import io.jsonwebtoken.JwtException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ExceptionResolver {

    @ExceptionHandler({AuthenticationException.class, BadCredentialsException.class, JwtException.class, UserNotFoundException.class})
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Error handleAuthenticationException(Exception ex) {
        return new Error(ex.getMessage());
    }

    @ExceptionHandler({AccessDeniedException.class})
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public Error handleAccessDeniedException(Exception ex) {
        return new Error(ex.getMessage());
    }

    @ExceptionHandler({EventNotFoundException.class, EventContractNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Error handleEventNotFoundException(Exception ex) {
        return new Error(ex.getMessage());
    }


    @ExceptionHandler({UserAlreadyExistsException.class})
    @ResponseStatus(HttpStatus.CONFLICT)
    public Error handleConflictException(Exception ex) {
        return new Error(ex.getMessage());
    }

//    @ExceptionHandler({Exception.class})
//    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
//    public Error handeException(Exception e) {
//        return new Error(e.getMessage());
//    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidation(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }

}