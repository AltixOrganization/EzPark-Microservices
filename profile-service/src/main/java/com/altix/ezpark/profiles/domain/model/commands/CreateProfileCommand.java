package com.altix.ezpark.profiles.domain.model.commands;

import java.time.LocalDate;

public record CreateProfileCommand(String firstName, String lastName, LocalDate birthDate, Long userId) {
    public CreateProfileCommand {
        //Name starts capital letter and no spaces
        if (firstName == null || !firstName.matches("^[A-Z][a-zA-Z]*$")) {
            throw new IllegalArgumentException("Name must start with a capital letter and contain no spaces");
        }
        //Last name starts capital letter and no spaces
        if (lastName == null || !lastName.matches("^[A-Z][a-zA-Z]*$")) {
            throw new IllegalArgumentException("Last name must start with a capital letter and contain no spaces");
        }
        if (birthDate == null) {
            throw new IllegalArgumentException("birthDate cannot be null or blank");
        }
        if (userId == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }
    }
}
