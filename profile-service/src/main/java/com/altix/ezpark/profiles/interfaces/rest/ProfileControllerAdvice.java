package com.altix.ezpark.profiles.interfaces.rest;

import static com.altix.ezpark.profiles.infrastructure.utils.ProfileErrorCatalog.*;

import com.altix.ezpark.interfaces.rest.resources.ErrorResource;
import com.altix.ezpark.profiles.domain.model.exceptions.*;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(basePackages = "com.altix.ezpark.profiles")
public class ProfileControllerAdvice {
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(ProfileNotFoundException.class)
    public ErrorResource handleProfileNotFoundException() {
        ErrorResource response = new ErrorResource();
        response.setCode(PROFILE_NOT_FOUND.getCode());
        response.setMessage(PROFILE_NOT_FOUND.getMessage());
        response.setTimeStamp(LocalDateTime.now());
        return response;
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(UserNotFoundException.class)
    public ErrorResource handleUserNotFoundException() {
        ErrorResource response = new ErrorResource();
        response.setCode(USER_NOT_FOUND.getCode());
        response.setMessage(USER_NOT_FOUND.getMessage());
        response.setTimeStamp(LocalDateTime.now());
        return response;
    }

    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler(ProfileUpdateException.class)
    public ErrorResource handleProfileUpdateException() {
        ErrorResource response = new ErrorResource();
        response.setCode(PROFILE_UPDATE_ERROR.getCode());
        response.setMessage(PROFILE_UPDATE_ERROR.getMessage());
        response.setTimeStamp(LocalDateTime.now());
        return response;
    }

    // invalid JSON
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ErrorResource handleHttpMessageNotReadableException() {
        ErrorResource response = new ErrorResource();
        response.setCode(INVALID_JSON.getCode());
        response.setMessage(INVALID_JSON.getMessage());
        response.setTimeStamp(LocalDateTime.now());
        return response;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorResource handleMethodArgumentNotValidException(
            MethodArgumentNotValidException exception) {
        BindingResult result = exception.getBindingResult();

        ErrorResource response = new ErrorResource();
        response.setCode(INVALID_PARAMETER.getCode());
        response.setMessage(INVALID_PARAMETER.getMessage());
        response.setDetails(
                result.getFieldErrors().stream()
                        .map(error -> error.getField() + ": " + error.getDefaultMessage())
                        .sorted()
                        .toList());
        response.setTimeStamp(LocalDateTime.now());
        return response;
    }



    // generic errors
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ErrorResource handleGenericException(Exception exception) {
        ErrorResource response = new ErrorResource();
        response.setCode(GENERIC_ERROR.getCode());
        response.setMessage(GENERIC_ERROR.getMessage());
        response.setDetails(List.of(exception.getMessage()));
        response.setTimeStamp(LocalDateTime.now());
        return response;
    }
}
