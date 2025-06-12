package com.altix.ezpark.reservations.application.internal.outboundservices.acl;

public interface VehiclesContextFacade {
    boolean checkVehicleExistsById(Long vehicleId) throws Exception;
}
