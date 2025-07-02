package com.altix.ezpark.profiles.application.dtos;

import java.time.LocalDate;
import java.time.LocalDateTime;


public record ProfileResponse(
        String correlationId,
        Long id,
        String firstName,
        String lastName,
        LocalDate birthDate,
        Long userId,
        String email,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
