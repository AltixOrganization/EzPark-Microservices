package com.altix.ezpark.profiles.infrastructure.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ProfileErrorCatalog {
    PROFILE_NOT_FOUND("ERR_PROFILE_001", "Profile not found"),
    USER_NOT_FOUND("ERR_PROFILE_002", "User not found"),
    PROFILE_UPDATE_ERROR("ERR_PROFILE_003", "Error while updating profile"),
    GENERIC_ERROR("ERR_PROFILE_999", "An unexpected error occurred"),
    // invalid parameters
    INVALID_PARAMETER("ERR_INVALID_001", "Invalid parameter"),

    // invalid JSON
    INVALID_JSON("ERR_JSON_001", "Invalid JSON");

    private final String code;
    private final String message;
}
