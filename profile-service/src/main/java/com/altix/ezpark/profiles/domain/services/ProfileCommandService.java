package com.altix.ezpark.profiles.domain.services;

import com.altix.ezpark.profiles.domain.model.aggregates.Profile;
import com.altix.ezpark.profiles.domain.model.commands.CreateProfileCommand;
import com.altix.ezpark.profiles.domain.model.commands.DeleteProfileCommand;
import com.altix.ezpark.profiles.domain.model.commands.UpdateProfileCommand;
import java.util.Optional;

public interface ProfileCommandService {
    Optional<Profile> handle(CreateProfileCommand command);
    Optional<Profile> handle(UpdateProfileCommand command);
    void handle(DeleteProfileCommand command);
}