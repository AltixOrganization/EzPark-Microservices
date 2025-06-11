package com.altix.ezpark.profiles.domain.services;

import com.altix.ezpark.profiles.domain.model.aggregates.Profile;
import com.altix.ezpark.profiles.domain.model.queries.GetAllProfilesQuery;
import com.altix.ezpark.profiles.domain.model.queries.GetProfileByIdQuery;
import java.util.List;
import java.util.Optional;

public interface ProfileQueryService {
    Optional<Profile> handle(GetProfileByIdQuery query);

    List<Profile> handle(GetAllProfilesQuery query);
}
