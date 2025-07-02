package com.altix.ezpark.notifications.application.internal.outboundservices.acl;

import com.altix.ezpark.parkings.application.dtos.ParkingForNotificationResponse;

public interface ParkingContextFacade {
    ParkingForNotificationResponse getParkingForNotification(
            Long parkingId,
            Long reservationId,
            Long scheduleId
    ) throws Exception;
}
