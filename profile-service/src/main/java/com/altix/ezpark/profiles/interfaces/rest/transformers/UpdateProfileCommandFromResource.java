package com.altix.ezpark.profiles.interfaces.rest.transformers;

import com.altix.ezpark.profiles.domain.model.commands.UpdateProfileCommand;
import com.altix.ezpark.profiles.interfaces.rest.resources.UpdateProfileResource;

public class UpdateProfileCommandFromResource {
    public static UpdateProfileCommand toCommandFromResource(Long userId, UpdateProfileResource resource) {
        return new UpdateProfileCommand(userId, resource.firstName(), resource.lastName(), resource.birthDate());
    }
}
