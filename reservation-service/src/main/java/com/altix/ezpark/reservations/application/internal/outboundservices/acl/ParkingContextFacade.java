package com.altix.ezpark.reservations.application.internal.outboundservices.acl;

public interface ParkingContextFacade {
    boolean checkParkingExistById(Long parkingId) throws Exception;
}
