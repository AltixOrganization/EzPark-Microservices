package com.altix.ezpark.reservations.domain.model.exceptions;

public class ScheduleUpdateException extends RuntimeException{
    public ScheduleUpdateException() {
        super("Failed to update the schedule.");
    }

    public ScheduleUpdateException(String message) {
        super(message);
    }

    public ScheduleUpdateException(String message, Throwable cause) {
        super(message, cause);
    }

    public ScheduleUpdateException(Throwable cause) {
        super(cause);
    }
}
