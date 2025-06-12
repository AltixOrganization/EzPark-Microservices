package com.altix.ezpark.payments.domain.model.exceptions;

/**
 * @author amner
 * @date 7/06/2025
 */
public class ExternalServiceCommunicationException extends RuntimeException {
    public ExternalServiceCommunicationException(String message) {
        super(message);
    }
    public ExternalServiceCommunicationException(String message, Throwable cause) {
        super(message, cause);
    }
}
