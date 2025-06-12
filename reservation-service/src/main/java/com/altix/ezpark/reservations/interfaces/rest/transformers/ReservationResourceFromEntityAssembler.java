package com.altix.ezpark.reservations.interfaces.rest.transformers;


import com.altix.ezpark.reservations.domain.model.aggregates.Reservation;
import com.altix.ezpark.reservations.interfaces.rest.resources.ReservationResource;

public class ReservationResourceFromEntityAssembler {
    public static ReservationResource toResourceFromEntity(Reservation entity) {
        return new ReservationResource(
                entity.getId(),
                entity.getHoursRegistered(),
                entity.getTotalFare(),
                entity.getReservationDate(),
                entity.getStartTime(),
                entity.getEndTime(),
                entity.getStatus().name(),
                entity.getGuestId().guestId(),
                entity.getHostId().hostId(),
                entity.getParkingId().parkingId(),
                entity.getVehicleId().vehicleId(),
                entity.getScheduleId().scheduleId(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}
