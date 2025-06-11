package com.altix.ezpark.profiles.application.internal.queryservices;

import com.altix.ezpark.profiles.domain.model.aggregates.Profile;
import com.altix.ezpark.profiles.domain.model.queries.GetAllProfilesQuery;
import com.altix.ezpark.profiles.domain.model.queries.GetProfileByIdQuery;
import com.altix.ezpark.profiles.domain.services.ProfileQueryService;
import com.altix.ezpark.profiles.infrastructure.persistence.jpa.repositories.ProfileRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class ProfileQueryServiceImpl implements ProfileQueryService {
    private final ProfileRepository profileRepository;

    public ProfileQueryServiceImpl(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    @Override
    public Optional<Profile> handle(GetProfileByIdQuery query)
    {
        return profileRepository.findById(query.userId());
    }

    @Override
    public List<Profile> handle(GetAllProfilesQuery query) {
        return profileRepository.findAll();
    }


}
