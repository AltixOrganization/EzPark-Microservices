package com.altix.ezpark.profiles.interfaces.rest.resources;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

public record CreateProfileResource(
        @NotNull(message = "{profile.firstName.not.null}")
        @NotBlank(message = "{profile.firstName.not.blank}")
        @Size(max = 200, message = "{profile.firstName.size}")
        String firstName,

        @NotNull(message = "{profile.lastName.not.null}")
        @NotBlank(message = "{profile.lastName.not.blank}")
        @Size(max = 200, message = "{profile.lastName.size}")
        String lastName,

        @NotNull(message = "{profile.birthDate.not.null}")
        @Past(message = "{profile.birthDate.past}")
        LocalDate birthDate,

        @NotNull(message = "{profile.userId.not.null}")
        Long userId
) {
}
