package com.altix.ezpark.profiles.application.internal.commandservices;


import com.altix.ezpark.profiles.domain.model.aggregates.Profile;
import com.altix.ezpark.profiles.domain.model.commands.CreateProfileCommand;
import com.altix.ezpark.profiles.domain.model.commands.DeleteProfileCommand;
import com.altix.ezpark.profiles.domain.model.commands.UpdateProfileCommand;
import com.altix.ezpark.profiles.domain.model.exceptions.*;
import com.altix.ezpark.profiles.domain.services.ProfileCommandService;
import com.altix.ezpark.profiles.infrastructure.persistence.jpa.repositories.ProfileRepository;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class ProfileCommandServiceImpl implements ProfileCommandService {

    private final ProfileRepository profileRepository;



    @Override
    public Optional<Profile> handle(CreateProfileCommand command) {
        Profile user = new Profile(command);
        try {
            var response = profileRepository.save(user);

            return Optional.of(response);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public Optional<Profile> handle(UpdateProfileCommand command) {
        var result = profileRepository.findById(command.profileId());
        if (result.isEmpty())
            throw new ProfileNotFoundException();
        var userToUpdate = result.get();
        try{
            var updatedUser= profileRepository.save(userToUpdate.updatedProfile(command));
            return Optional.of(updatedUser);
        }catch (Exception e){
            throw new ProfileUpdateException();
        }
    }

    @Override
    public void handle(DeleteProfileCommand command){
        if (!profileRepository.existsById(command.profileId())) throw new ProfileNotFoundException();
        profileRepository.deleteById(command.profileId());
        System.out.println("User Delete");
    }

}
