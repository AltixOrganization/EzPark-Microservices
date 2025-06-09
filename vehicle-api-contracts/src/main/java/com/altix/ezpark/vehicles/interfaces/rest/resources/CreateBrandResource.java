package com.altix.ezpark.vehicles.interfaces.rest.resources;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateBrandResource(
        @NotBlank(message = "{brand.name.not.blank}")
        @Size(max = 100, message = "{brand.name.size}")
        String name,

        @Size(max = 500, message = "{brand.description.size}")
        String description
) {
}
