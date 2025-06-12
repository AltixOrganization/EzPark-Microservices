package com.altix.ezpark.parkings.application.internal.outboundservices.acl;

public interface ProfileContextFacade {
    boolean checkProfileExistById(Long profileId) throws Exception;
    //Long createProfile(Long userId, String firstName, String lastName, LocalDate birthDate) throws Exception;
}
