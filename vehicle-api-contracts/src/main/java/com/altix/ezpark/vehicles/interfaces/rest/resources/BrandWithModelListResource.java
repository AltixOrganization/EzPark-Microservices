package com.altix.ezpark.vehicles.interfaces.rest.resources;

import java.util.List;

public record BrandWithModelListResource(
        Long id,
        String name,
        String description,
        List<ModelResource> models
) {
}
