package com.altix.ezpark.parkings.application.dtos;

import java.time.LocalDate;
import java.time.LocalTime;

public record ParkingForNotificationResponse(
        String correlationId,
        String phone,
        String address,
        String numDirection,
        String street,
        String district,
        String city,
        LocalDate day,
        LocalTime startTime,
        LocalTime endTime,
        boolean success,
        String message
) {
}