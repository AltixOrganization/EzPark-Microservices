package com.altix.ezpark.notifications.interfaces.rest.resources;


public record EmailLogResource(
    Long id,
    String recipient,
    String subject,
    String status,
    Long reservationId,
    String sentAt,
    String errorMessage
) {
}
