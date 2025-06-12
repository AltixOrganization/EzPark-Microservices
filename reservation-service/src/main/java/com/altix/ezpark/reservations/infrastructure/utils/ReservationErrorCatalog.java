package com.altix.ezpark.reservations.infrastructure.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ReservationErrorCatalog {
    PROFILE_NOT_FOUND("ERR_RESERVATION_001", "Profile not found (Guest or Host)"),
    VEHICLE_NOT_FOUND("ERR_RESERVATION_002", "Vehicle not found"),
    PARKING_NOT_FOUND("ERR_RESERVATION_003", "Parking not found"),
    RESERVATION_NOT_FOUND("ERR_RESERVATION_004", "Reservation not found"),
    SCHEDULE_CONFLICT("ERR_RESERVATION_005", "The schedule does not include the requested time range"),
    RESERVATION_OVERLAP("ERR_RESERVATION_006", "The requested schedule overlaps with an already approved reservation"),
    EMPTY_FILE("ERR_RESERVATION_007", "Empty file"),
    IMAGE_UPLOAD_ERROR("ERR_RESERVATION_008", "Image upload error"),
    RESERVATION_UPDATE_ERROR("ERR_RESERVATION_009", "Reservation update error"),
    RESERVATION_SCHEDULE_UPDATE_ERROR("ERR_RESERVATION_010", "Failed to mark schedule as unavailable when approving reservation"),


    // invalid parameters
    INVALID_PARAMETER("ERR_INVALID_001", "Invalid parameter"),

    // invalid JSON
    INVALID_JSON("ERR_JSON_001", "Invalid JSON"),

    // generic errors
    GENERIC_ERROR("ERR_GENERIC_001", "Generic error");

    private final String code;
    private final String message;

}
