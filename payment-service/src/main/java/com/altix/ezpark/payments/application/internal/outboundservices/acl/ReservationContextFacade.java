package com.altix.ezpark.payments.application.internal.outboundservices.acl;

public interface ReservationContextFacade {
    void approveReservation(Long reservationId) throws Exception;
}
