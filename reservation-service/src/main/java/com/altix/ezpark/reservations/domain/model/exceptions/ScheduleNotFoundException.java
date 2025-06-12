package com.altix.ezpark.reservations.domain.model.exceptions;

/**
 * @author amner
 * @date 7/06/2025
 */
public class ScheduleNotFoundException extends RuntimeException {
    public ScheduleNotFoundException() {}
    public ScheduleNotFoundException(String message) {
        super(message);
    }
}
