package com.altix.ezpark.reservations.interfaces.rest;

import com.altix.ezpark.interfaces.rest.resources.ErrorResource;
import com.altix.ezpark.reservations.domain.model.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.List;

import static com.altix.ezpark.reservations.infrastructure.utils.ReservationErrorCatalog.*;

@RestControllerAdvice(basePackages = "com.altix.ezpark.reservations")
public class ReservationControllerAdvice {
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
    @ExceptionHandler(ReservationNotFoundException.class)
    public ErrorResource handleReservationNotFoundException() {
        ErrorResource response = new ErrorResource();
        response.setCode(RESERVATION_NOT_FOUND.getCode());
        response.setMessage(RESERVATION_NOT_FOUND.getMessage());
        response.setTimeStamp(LocalDateTime.now());
        return response;
    }

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
    @ExceptionHandler(VehicleNotFoundException.class)
    public ErrorResource handleVehicleNotFoundException() {
        ErrorResource response = new ErrorResource();
        response.setCode(VEHICLE_NOT_FOUND.getCode());
        response.setMessage(VEHICLE_NOT_FOUND.getMessage());
        response.setTimeStamp(LocalDateTime.now());
        return response;
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(ScheduleConflictException.class)
    public ErrorResource handleScheduleConflictException() {
        ErrorResource response = new ErrorResource();
        response.setCode(SCHEDULE_CONFLICT.getCode());
        response.setMessage(SCHEDULE_CONFLICT.getMessage());
        response.setTimeStamp(LocalDateTime.now());
        return response;
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(ReservationOverlapException.class)
    public ErrorResource handleReservationOverlapException() {
        ErrorResource response = new ErrorResource();
        response.setCode(RESERVATION_OVERLAP.getCode());
        response.setMessage(RESERVATION_OVERLAP.getMessage());
        response.setTimeStamp(LocalDateTime.now());
        return response;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(EmptyFileException.class)
    public ErrorResource handleEmptyFileException() {
        ErrorResource response = new ErrorResource();
        response.setCode(EMPTY_FILE.getCode());
        response.setMessage(EMPTY_FILE.getMessage());
        response.setTimeStamp(LocalDateTime.now());
        return response;
    }

    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler(ImageUploadException.class)
    public ErrorResource handleImageUploadException() {
        ErrorResource response = new ErrorResource();
        response.setCode(IMAGE_UPLOAD_ERROR.getCode());
        response.setMessage(IMAGE_UPLOAD_ERROR.getMessage());
        response.setTimeStamp(LocalDateTime.now());
        return response;
    }

    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler(ReservationUpdateException.class)
    public ErrorResource handleReservationUpdateException() {
        ErrorResource response = new ErrorResource();
        response.setCode(RESERVATION_UPDATE_ERROR.getCode());
        response.setMessage(RESERVATION_UPDATE_ERROR.getMessage());
        response.setTimeStamp(LocalDateTime.now());
        return response;
    }

    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler(ScheduleUpdateException.class)
    public ErrorResource handleScheduleUpdateException() {
        ErrorResource response = new ErrorResource();
        response.setCode(RESERVATION_SCHEDULE_UPDATE_ERROR.getCode());
        response.setMessage(RESERVATION_SCHEDULE_UPDATE_ERROR.getMessage());
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
