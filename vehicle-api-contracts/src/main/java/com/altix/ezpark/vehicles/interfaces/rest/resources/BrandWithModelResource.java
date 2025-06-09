package com.altix.ezpark.vehicles.interfaces.rest.resources;


public record BrandWithModelResource(
        Long id,
        String name,
        String description,
        ModelResource model
) {
}
