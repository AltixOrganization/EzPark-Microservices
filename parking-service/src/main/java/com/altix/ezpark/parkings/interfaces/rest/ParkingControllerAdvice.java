package com.altix.ezpark.parkings.interfaces.rest;

import com.altix.ezpark.parkings.domain.model.exceptions.*;
import com.altix.ezpark.shared.interfaces.rest.resources.ErrorResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.List;

import static com.altix.ezpark.parkings.infrastructure.utils.ParkingErrorCatalog.*;


@RestControllerAdvice(basePackages = "com.altix.ezpark.parkings")
public class ParkingControllerAdvice {
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(ParkingNotFoundException.class)
    public ErrorResource handleParkingNotFoundException() {
        ErrorResource response = new ErrorResource();
        response.setCode(PARKING_NOT_FOUND.getCode());
        response.setMessage(PARKING_NOT_FOUND.getMessage());
        response.setTimeStamp(LocalDateTime.now());
        return response;
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(ScheduleNotFoundException.class)
    public ErrorResource handleScheduleNotFoundException() {
        ErrorResource response = new ErrorResource();
        response.setCode(SCHEDULE_NOT_FOUND.getCode());
        response.setMessage(SCHEDULE_NOT_FOUND.getMessage());
        response.setTimeStamp(LocalDateTime.now());
        return response;
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(LocationNotFoundException.class)
    public ErrorResource handleLocationNotFoundException() {
        ErrorResource response = new ErrorResource();
        response.setCode(LOCATION_NOT_FOUND.getCode());
        response.setMessage(LOCATION_NOT_FOUND.getMessage());
        response.setTimeStamp(LocalDateTime.now());
        return response;
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(ProfileNotFoundException.class)
    public ErrorResource handleProfileNotFoundException() {
        ErrorResource response = new ErrorResource();
        response.setCode(PROFILE_NOT_FOUND.getCode());
        response.setMessage(PROFILE_NOT_FOUND.getMessage());
        response.setTimeStamp(LocalDateTime.now());
        return response;
    }

    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler({ParkingUpdateException.class, LocationUpdateException.class, ScheduleUpdateException.class})
    public ErrorResource handleUpdateExceptions(Exception ex) {
        ErrorResource response = new ErrorResource();
        if (ex instanceof ParkingUpdateException) {
            response.setCode(PARKING_UPDATE_ERROR.getCode());
            response.setMessage(PARKING_UPDATE_ERROR.getMessage());
        } else if (ex instanceof LocationUpdateException) {
            response.setCode(LOCATION_UPDATE_ERROR.getCode());
            response.setMessage(LOCATION_UPDATE_ERROR.getMessage());
        } else {
            response.setCode(SCHEDULE_UPDATE_ERROR.getCode());
            response.setMessage(SCHEDULE_UPDATE_ERROR.getMessage());
        }
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
