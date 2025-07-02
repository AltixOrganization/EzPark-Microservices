package com.altix.ezpark.profiles.interfaces.rest.transformers;

import com.altix.ezpark.profiles.domain.model.commands.CreateProfileCommand;
import com.altix.ezpark.profiles.interfaces.rest.resources.CreateProfileResource;

public class CreateProfileCommandFromResourceAssembler {
    public static CreateProfileCommand toCommandFromResource(CreateProfileResource resource) {
        return new CreateProfileCommand(
                resource.firstName(),
                resource.lastName(),
                resource.birthDate(),
                resource.email(),
                resource.userId()
        );
    }
}
