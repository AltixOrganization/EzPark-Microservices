package com.altix.ezpark.notifications.domain.model.commands;

import com.altix.ezpark.notifications.domain.model.valueobjects.SendStatus;

/**
 * @author amner
 * @date 3/07/2025
 */
public record CreateEmailLogCommand(String recipient, String subject, SendStatus status, Long reservationId, String errorMessage) {

    public CreateEmailLogCommand(String recipient, String subject, SendStatus status, Long reservationId) {
        this(recipient, subject, status, reservationId, null);
    }
}
