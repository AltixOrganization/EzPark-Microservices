package com.altix.ezpark.reviews.application.internal.outboundservices.acl;

public interface ParkingContextFacade {
    boolean checkParkingExistById(Long parkingId) throws Exception;
}
