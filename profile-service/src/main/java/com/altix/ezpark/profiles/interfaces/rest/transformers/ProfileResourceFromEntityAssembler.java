package com.altix.ezpark.profiles.interfaces.rest.transformers;

import com.altix.ezpark.profiles.domain.model.aggregates.Profile;
import com.altix.ezpark.profiles.interfaces.rest.resources.ProfileResource;

public class ProfileResourceFromEntityAssembler {
    public static ProfileResource toResourceFromEntity(Profile entity) {
        return new ProfileResource(
                entity.getId(),
                entity.getFirstName(),
                entity.getLastName(),
                entity.getBirthDate(),
                entity.getUserId().userIdAsPrimitive(),
                entity.getEmail(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}
