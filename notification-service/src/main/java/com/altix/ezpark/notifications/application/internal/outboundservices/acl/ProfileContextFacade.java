package com.altix.ezpark.notifications.application.internal.outboundservices.acl;

import com.altix.ezpark.profiles.application.dtos.ProfileResponse;

public interface ProfileContextFacade {
    ProfileResponse getProfileById(Long profileId) throws Exception;
}
