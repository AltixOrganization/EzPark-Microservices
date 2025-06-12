package com.altix.ezpark.profiles.application.acl;
//
//import com.altix.ezpark.profiles.domain.model.commands.CreateProfileCommand;
//import com.altix.ezpark.profiles.domain.model.queries.GetProfileByIdQuery;
//import com.altix.ezpark.profiles.domain.services.ProfileCommandService;
//import com.altix.ezpark.profiles.domain.services.ProfileQueryService;
//import com.altix.ezpark.parkings.application.internal.outboundservices.acl.ProfileContextFacade;
//import org.springframework.stereotype.Service;
//
//import java.time.LocalDate;
//
//@Service
//public class ProfileContextFacadeImpl implements ProfileContextFacade {
//    private final ProfileQueryService profileQueryService;
//    private final ProfileCommandService profileCommandService;
//
//    public ProfileContextFacadeImpl(ProfileQueryService profileQueryService, ProfileCommandService profileCommandService) {
//        this.profileQueryService = profileQueryService;
//        this.profileCommandService = profileCommandService;
//    }
//
//    @Override
//    public boolean checkProfileExistById(Long profileId) {
//        var getProfileByIdQuery = new GetProfileByIdQuery(profileId);
//        var profile = profileQueryService.handle(getProfileByIdQuery);
//        return profile.isPresent();
//    }
//
//    @Override
//    public Long createProfile(Long userId, String firstName, String lastName, LocalDate birthDate) {
//        var createProfileCommand = new CreateProfileCommand(firstName, lastName, birthDate, userId);
//        var result = profileCommandService.handle(createProfileCommand);
//        if (result.isEmpty()) {
//            return 0L; // or throw an exception based on your application's error handling strategy
//        }
//        return result.get().getId();
//    }
//}
