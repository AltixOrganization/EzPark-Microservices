package com.altix.ezpark.parkings.interfaces.rest.resources;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdateLocationResource(
        @NotBlank(message = "{location.address.not.blank}")
        String address,

        @NotNull(message = "{location.numDirection.not.null}")
        @NotBlank(message = "{location.numDirection.not.blank}")
        String numDirection,

        @NotNull(message = "{location.street.not.null}")
        @NotBlank(message = "{location.street.not.blank}")
        String street,

        @NotNull(message = "{location.district.not.null}")
        @NotBlank(message = "{location.district.not.blank}")
        String district,

        @NotNull(message = "{location.city.not.null}")
        @NotBlank(message = "{location.city.not.blank}")
        String city,

        @NotNull(message = "{location.latitude.not.null}")
        @Min(value = -90, message = "{location.latitude.min}")
        @Max(value = 90, message = "{location.latitude.max}")
        Double latitude,

        @NotNull(message = "{location.longitude.not.null}")
        @Min(value = -180, message = "{location.longitude.min}")
        @Max(value = 180, message = "{location.longitude.max}")
        Double longitude
) {
}
