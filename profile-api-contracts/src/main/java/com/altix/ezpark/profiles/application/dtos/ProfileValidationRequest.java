package com.altix.ezpark.profiles.application.dtos;

public record ProfileValidationRequest(
        String correlationId,
        Long profileId
) {
}
