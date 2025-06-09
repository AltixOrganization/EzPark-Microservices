package com.altix.ezpark.vehicles.interfaces.rest.resources;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UpdateModelResource(
        @NotNull(message = "{model.id.not.null}")
        Long id,

        @NotNull(message = "{model.brandId.not.null}")
        Long brandId,

        @NotBlank(message = "{model.name.not.blank}")
        @Size(max = 100, message = "{model.name.size}")
        String name,

        @Size(max = 500, message = "{model.description.size}")
        String description
) {
}
