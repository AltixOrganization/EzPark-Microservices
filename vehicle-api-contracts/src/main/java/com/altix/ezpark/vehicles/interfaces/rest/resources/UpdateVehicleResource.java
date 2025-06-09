package com.altix.ezpark.vehicles.interfaces.rest.resources;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record UpdateVehicleResource(
        @NotBlank(message = "{vehicle.licensePlate.not.blank}")
        @Pattern(regexp = "^[A-Z0-9]{6,8}$", message = "{vehicle.licensePlate.pattern}")
        String licensePlate,

        @NotNull(message = "{model.id.not.null}")
        Long modelId
) {
}
