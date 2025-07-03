package com.altix.ezpark.notifications.application.internal.outboundservices;

import com.altix.ezpark.notifications.domain.model.valueobjects.UserRole;
import com.altix.ezpark.parkings.application.dtos.ParkingForNotificationResponse;
import com.altix.ezpark.profiles.application.dtos.ProfileResponse;

import java.time.LocalDate;

public interface EmailTemplateService {
    public String buildEmailContent(
            UserRole userRole,
            String previousStatus,
            String newStatus,
            ProfileResponse recipient,
            ProfileResponse otherParty,
            ParkingForNotificationResponse parkingInfo,
            LocalDate reservationDate,
            Long reservationId
    );
    String getSubject(UserRole userRole, String newStatus, String location);
}
