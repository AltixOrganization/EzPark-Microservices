package com.altix.ezpark.notifications.interfaces.rest.transform;


import com.altix.ezpark.notifications.domain.model.aggregates.EmailLog;
import com.altix.ezpark.notifications.interfaces.rest.resources.EmailLogResource;

public class EmailLogResourceFromEntityAssembler {
    public static EmailLogResource toResourceFromEntity(EmailLog entity) {
        return new EmailLogResource(
                entity.getId(),
                entity.getRecipient(),
                entity.getSubject(),
                entity.getStatus().name(),
                entity.getReservationId(),
                entity.getSentAt().toString(),
                entity.getErrorMessage()
        );
    }
}
