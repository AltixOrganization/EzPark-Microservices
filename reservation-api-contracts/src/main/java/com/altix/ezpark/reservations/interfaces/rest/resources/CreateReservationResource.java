package com.altix.ezpark.reservations.interfaces.rest.resources;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.LocalDate;
import java.time.LocalTime;

public record CreateReservationResource(
        @NotNull(message = "{reservation.hoursRegistered.not.null}")
        @Min(value = 1, message = "{reservation.hoursRegistered.min}")
        Integer hoursRegistered,

        @NotNull(message = "{reservation.totalFare.not.null}")
        @Positive(message = "{reservation.totalFare.positive}")
        Double totalFare,

        @NotNull(message = "{reservation.reservationDate.not.null}")
        LocalDate reservationDate,

        @NotNull(message = "{reservation.startTime.not.null}")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
        LocalTime startTime,

        @NotNull(message = "{reservation.endTime.not.null}")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
        LocalTime endTime,

        @NotNull(message = "{reservation.guestId.not.null}")
        Long guestId,

        @NotNull(message = "{reservation.hostId.not.null}")
        Long hostId,

        @NotNull(message = "{reservation.parkingId.not.null}")
        Long parkingId,

        @NotNull(message = "{reservation.vehicleId.not.null}")
        Long vehicleId,

        @NotNull(message = "{reservation.scheduleId.not.null}")
        Long scheduleId
) {}
