package com.altix.ezpark.profiles.application.dtos;

public record ProfileValidationResponse(
        String correlationId,
        Long profileId,
        boolean exists
) {
}
