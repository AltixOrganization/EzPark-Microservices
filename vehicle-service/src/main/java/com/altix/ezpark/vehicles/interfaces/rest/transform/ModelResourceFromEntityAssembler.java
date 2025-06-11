package com.altix.ezpark.vehicles.interfaces.rest.transform;

import com.altix.ezpark.vehicles.domain.model.entities.Model;
import com.altix.ezpark.vehicles.interfaces.rest.resources.ModelResource;

import java.util.List;

public class ModelResourceFromEntityAssembler {
    public static ModelResource toResourceFromEntity(Model model) {
        return new ModelResource(
                model.getId(),
                model.getName(),
                model.getDescription(),
                model.getBrand().getId()
        );
    }

    public static List<ModelResource> toResourceListFromEntityList(List<Model> models) {
        return models.stream()
                .map(ModelResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
    }
}
