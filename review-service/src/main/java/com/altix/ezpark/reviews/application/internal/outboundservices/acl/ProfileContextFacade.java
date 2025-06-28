package com.altix.ezpark.reviews.application.internal.outboundservices.acl;

public interface ProfileContextFacade {
    boolean checkProfileExistById(Long userId) throws Exception;
}
