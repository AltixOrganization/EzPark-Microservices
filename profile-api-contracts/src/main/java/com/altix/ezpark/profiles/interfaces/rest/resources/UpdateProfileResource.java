package com.altix.ezpark.profiles.interfaces.rest.resources;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

public record UpdateProfileResource(
        @NotBlank(message = "{profile.firstName.not.blank}")
        @Size(max = 200, message = "{profile.firstName.size}")
        String firstName,

        @NotBlank(message = "{profile.lastName.not.blank}")
        @Size(max = 200, message = "{profile.lastName.size}")
        String lastName,

        @Past(message = "{profile.birthDate.past}")
        LocalDate birthDate
) {
}
