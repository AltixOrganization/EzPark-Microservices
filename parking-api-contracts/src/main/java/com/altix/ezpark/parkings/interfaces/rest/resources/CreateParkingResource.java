package com.altix.ezpark.parkings.interfaces.rest.resources;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

public record CreateParkingResource(
        @NotNull(message = "{parking.profileId.not.null}")
        Long profileId,

        @NotNull(message = "{parking.width.not.null}")
        @Positive(message = "{parking.width.positive}")
        Double width,

        @NotNull(message = "{parking.length.not.null}")
        @Positive(message = "{parking.length.positive}")
        Double length,

        @NotNull(message = "{parking.height.not.null}")
        @Positive(message = "{parking.height.positive}")
        Double height,

        @NotNull(message = "{parking.price.not.null}")
        @Positive(message = "{parking.price.positive}")
        Double price,

        @NotNull(message = "{parking.phone.not.null}")
        @NotBlank(message = "{parking.phone.not.blank}")
        @Pattern(regexp = "^\\+?[0-9]{9,15}$", message = "{parking.phone.invalid}")
        String phone,

        @NotNull(message = "{parking.space.not.null}")
        @Min(value = 1, message = "{parking.space.min}")
        Integer space,

        @Size(max = 500, message = "{parking.description.size}")
        String description,

        @NotNull(message = "{parking.location.not.null}")
        @Valid
        CreateLocationResource location
) {
}
