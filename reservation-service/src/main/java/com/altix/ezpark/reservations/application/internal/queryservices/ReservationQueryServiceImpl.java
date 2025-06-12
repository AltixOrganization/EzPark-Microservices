package com.altix.ezpark.reservations.application.internal.queryservices;

import com.altix.ezpark.reservations.domain.model.aggregates.Reservation;
import com.altix.ezpark.reservations.domain.model.queries.*;
import com.altix.ezpark.reservations.domain.model.valueobject.GuestId;
import com.altix.ezpark.reservations.domain.model.valueobject.HostId;
import com.altix.ezpark.reservations.domain.model.valueobject.Status;
import com.altix.ezpark.reservations.domain.services.ReservationQueryService;
import com.altix.ezpark.reservations.infrastructure.persistence.jpa.repositories.ReservationRepository;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class ReservationQueryServiceImpl implements ReservationQueryService {

    private final ReservationRepository reservationRepository;

    public ReservationQueryServiceImpl(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    @Override
    public List<Reservation> handle(GetAllReservationsQuery query) {
        return reservationRepository.findAll();
    }

    @Override
    public Optional<Reservation> handle(GetReservationByIdQuery query) {
        return reservationRepository.findById(query.reservationId());
    }

    @Override
    public List<Reservation> handle(GetInProgressReservationQuery query) {
        return reservationRepository.findByStatus(Status.InProgress);
    }

    @Override
    public List<Reservation> handle(GetPastReservationQuery query) {
        List<Status> pastStatuses = Arrays.asList(Status.Completed, Status.Cancelled);
        return reservationRepository.findByStatusIn(pastStatuses);
    }

    @Override
    public List<Reservation> handle(GetUpComingReservationQuery query) {
        List<Status> comingStatuses = Arrays.asList(Status.Pending, Status.Approved);
        return reservationRepository.findByStatusIn(comingStatuses);
    }

    @Override
    public List<Reservation> handle(GetReservationsByHostIdQuery query) {
        HostId hostId = new HostId(query.hostId());
        return reservationRepository.findByHostId(hostId);
    }

    @Override
    public List<Reservation> handle(GetReservationsByGuestIdQuery query) {
        GuestId guestId = new GuestId(query.guestId());
        return reservationRepository.findByGuestId(guestId);
    }
}
