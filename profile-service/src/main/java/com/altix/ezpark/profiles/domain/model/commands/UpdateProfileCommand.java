package com.altix.ezpark.profiles.domain.model.commands;

import java.time.LocalDate;

public record UpdateProfileCommand(Long profileId, String firstName, String lastName, LocalDate birthDate) {
}
