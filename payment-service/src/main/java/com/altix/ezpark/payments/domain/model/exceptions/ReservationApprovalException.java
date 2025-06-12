package com.altix.ezpark.payments.domain.model.exceptions;

public class ReservationApprovalException extends RuntimeException{
    public ReservationApprovalException(String message) {
        super(message);
    }

    public ReservationApprovalException(String message, Throwable cause) {
        super(message, cause);
    }

    public ReservationApprovalException(Throwable cause) {
        super(cause);
    }
}
