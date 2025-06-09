package com.altix.ezpark.parkings.interfaces.rest.resources;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record UpdateParkingResource(
        @Positive(message = "{parking.width.positive}")
        Double width,

        @Positive(message = "{parking.length.positive}")
        Double length,

        @Positive(message = "{parking.height.positive}")
        Double height,

        @Positive(message = "{parking.price.positive}")
        Double price,

        @Pattern(regexp = "^\\+?[0-9]{9,15}$", message = "{parking.phone.invalid}")
        String phone,

        @Min(value = 1, message = "{parking.space.min}")
        Integer space,

        @Size(max = 500, message = "{parking.description.size}")
        String description,

        CreateLocationResource location
) {
}
