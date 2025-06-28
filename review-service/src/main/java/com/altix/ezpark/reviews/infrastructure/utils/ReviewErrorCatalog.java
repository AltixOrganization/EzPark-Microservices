package com.altix.ezpark.reviews.infrastructure.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ReviewErrorCatalog {
    PROFILE_NOT_FOUND("ERR_REVIEW_001", "Profile not found"),
    PARKING_NOT_FOUND("ERR_REVIEW_002", "Parking not found"),
    REVIEW_NOT_FOUND("ERR_REVIEW_003", "Review not found"),


    INVALID_PARAMETER("ERR_INVALID_001", "Invalid parameter"),

    INVALID_JSON("ERR_JSON_001", "Invalid JSON"),

    GENERIC_ERROR("ERR_GENERIC_001", "Generic error");

    private final String code;
    private final String message;
}
