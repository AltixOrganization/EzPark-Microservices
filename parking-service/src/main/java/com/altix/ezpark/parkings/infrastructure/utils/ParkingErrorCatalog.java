package com.altix.ezpark.parkings.infrastructure.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ParkingErrorCatalog {
    PARKING_NOT_FOUND("ERR_PARKING_001", "Parking not found"),
    SCHEDULE_NOT_FOUND("ERR_PARKING_002", "Schedule not found"),
    LOCATION_NOT_FOUND("ERR_PARKING_003", "Location not found"),
    PROFILE_NOT_FOUND("ERR_PARKING_004", "Profile not found"),
    PARKING_UPDATE_ERROR("ERR_PARKING_005", "Error while updating parking"),
    LOCATION_UPDATE_ERROR("ERR_PARKING_006", "Error while updating location"),
    SCHEDULE_UPDATE_ERROR("ERR_PARKING_007", "Error while updating schedule"),
    GENERIC_ERROR("ERR_PARKING_999", "An unexpected error occurred"),
    // invalid parameters
    INVALID_PARAMETER("ERR_INVALID_001", "Invalid parameter"),

    // invalid JSON
    INVALID_JSON("ERR_JSON_001", "Invalid JSON");

    private final String code;
    private final String message;
}
