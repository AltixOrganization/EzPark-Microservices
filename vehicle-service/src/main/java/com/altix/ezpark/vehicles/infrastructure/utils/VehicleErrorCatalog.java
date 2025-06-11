package com.altix.ezpark.vehicles.infrastructure.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum VehicleErrorCatalog {
    VEHICLE_NOT_FOUND("ERR_VEHICLE_001", "Vehicle not found"),
    PROFILE_NOT_FOUND("ERR_VEHICLE_002", "Profile not found"),
    LICENSE_PLATE_CONFLICT("ERR_VEHICLE_003", "A vehicle with this license plate already exists"),
    VEHICLE_UPDATE_ERROR("ERR_VEHICLE_004", "Error while updating vehicle"),
    BRAND_NOT_FOUND("ERR_VEHICLE_005", "Brand not found"),
    MODEL_NOT_FOUND("ERR_VEHICLE_006", "Model not found"),
    // invalid parameters
    INVALID_PARAMETER("ERR_INVALID_001", "Invalid parameter"),

    // invalid JSON
    INVALID_JSON("ERR_JSON_001", "Invalid JSON"),

    // generic errors
    GENERIC_ERROR("ERR_GENERIC_001", "Generic error");

    private final String code;
    private final String message;
}
