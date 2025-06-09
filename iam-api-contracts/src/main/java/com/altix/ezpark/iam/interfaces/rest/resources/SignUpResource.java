package com.altix.ezpark.iam.interfaces.rest.resources;

import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.util.List;

public record SignUpResource(
        @NotNull(message = "{user.firstName.not.null}")
        @NotBlank(message = "{user.firstName.not.blank}")
        @Size(max = 200, message = "{user.firstName.size}")
        String firstName,

        @NotNull(message = "{user.lastName.not.null}")
        @NotBlank(message = "{user.lastName.not.blank}")
        @Size(max = 200, message = "{user.lastName.size}")
        String lastName,

        @NotNull(message = "{user.birthDate.not.null}")
        @Past(message = "{user.birthDate.past}")
        LocalDate birthDate,

        @NotNull(message = "{user.email.not.null}")
        @NotBlank(message = "{user.email.not.blank}")
        @Email(message = "{user.email.invalid}")
        String email,

        @NotNull(message = "{user.password.not.null}")
        @NotBlank(message = "{user.password.not.blank}")
        @Size(min = 8, max = 30, message = "{user.password.size}")
        @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).+$", message = "{user.password.pattern}")
        String password,

        @NotNull(message = "{user.roles.not.null}")
        List<String> roles
) {
}

