package com.altix.ezpark.iam.domain.model.commands;

import com.altix.ezpark.iam.domain.model.entities.Role;
import java.time.LocalDate;
import java.util.List;

public record SignUpCommand(
        String firstName,
        String lastName,
        LocalDate birthDate,
        String email,
        String password,
        List<Role> roles) {
}
