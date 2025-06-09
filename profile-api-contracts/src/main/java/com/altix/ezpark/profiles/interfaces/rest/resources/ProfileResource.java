package com.altix.ezpark.profiles.interfaces.rest.resources;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record ProfileResource(
        Long id,
        String firstName,
        String lastName,
        LocalDate birthDate,
        Long userId,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
