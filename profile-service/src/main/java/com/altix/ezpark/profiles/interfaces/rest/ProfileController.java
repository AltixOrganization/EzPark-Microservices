package com.altix.ezpark.profiles.interfaces.rest;

import com.altix.ezpark.profiles.domain.model.commands.DeleteProfileCommand;
import com.altix.ezpark.profiles.domain.model.queries.GetAllProfilesQuery;
import com.altix.ezpark.profiles.domain.model.queries.GetProfileByIdQuery;
import com.altix.ezpark.profiles.domain.services.ProfileCommandService;
import com.altix.ezpark.profiles.domain.services.ProfileQueryService;
import com.altix.ezpark.profiles.interfaces.rest.resources.CreateProfileResource;
import com.altix.ezpark.profiles.interfaces.rest.resources.ProfileResource;
import com.altix.ezpark.profiles.interfaces.rest.resources.UpdateProfileResource;
import com.altix.ezpark.profiles.interfaces.rest.transformers.CreateProfileCommandFromResourceAssembler;
import com.altix.ezpark.profiles.interfaces.rest.transformers.ProfileResourceFromEntityAssembler;
import com.altix.ezpark.profiles.interfaces.rest.transformers.UpdateProfileCommandFromResource;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "api/v1/profiles")
public class ProfileController {
    private final ProfileQueryService profileQueryService;
    private final ProfileCommandService profileCommandService;

    public ProfileController(ProfileQueryService profileQueryService, ProfileCommandService profileCommandService) {
        this.profileQueryService = profileQueryService;
        this.profileCommandService = profileCommandService;
    }

    @GetMapping
    public ResponseEntity<List<ProfileResource>> getAllProfiles() {

        var getAllProfilesQuery = new GetAllProfilesQuery();
        var profiles = profileQueryService.handle(getAllProfilesQuery).stream().map(ProfileResourceFromEntityAssembler::toResourceFromEntity).toList();

        return new ResponseEntity<>(profiles, HttpStatus.OK);
    }


    @GetMapping("/{id}")
    public ResponseEntity<ProfileResource> getProfileById(@PathVariable("id") Long id) {
        var getProfileByIdQuery = new GetProfileByIdQuery(id);

        var profile = profileQueryService.handle(getProfileByIdQuery).map(ProfileResourceFromEntityAssembler::toResourceFromEntity);

        return profile.map(u -> new ResponseEntity<>(u, HttpStatus.OK)).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<ProfileResource> createProfile(@Valid @RequestBody CreateProfileResource createProfileResource) {
        var createProfileCommand = CreateProfileCommandFromResourceAssembler.toCommandFromResource(createProfileResource);

        var profile = profileCommandService.handle(createProfileCommand).map(ProfileResourceFromEntityAssembler::toResourceFromEntity);

        return profile.map(u -> new ResponseEntity<>(u, HttpStatus.CREATED)).orElse(new ResponseEntity<>(HttpStatus.BAD_REQUEST));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProfileResource> updateProfile(@Valid@PathVariable Long id, @RequestBody UpdateProfileResource updateProfileResource) {
        var updateProfileCommand = UpdateProfileCommandFromResource.toCommandFromResource(id, updateProfileResource);
        var updatedProfile = profileCommandService.handle(updateProfileCommand).map(ProfileResourceFromEntityAssembler::toResourceFromEntity);
        return updatedProfile.map(r -> new ResponseEntity<>(r, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteProfile(@PathVariable Long id){
        var deleteProfileCommand = new DeleteProfileCommand(id);
        profileCommandService.handle(deleteProfileCommand);
        return ResponseEntity.noContent().build();
    }

}
