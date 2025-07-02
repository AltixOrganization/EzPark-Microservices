package com.altix.ezpark.reservations.application.internal.commandservices;

import com.altix.ezpark.reservations.application.internal.outboundservices.acl.*;
import com.altix.ezpark.reservations.domain.model.aggregates.Reservation;
import com.altix.ezpark.reservations.domain.model.commands.CreateReservationCommand;
import com.altix.ezpark.reservations.domain.model.commands.UpdateReservationCommand;
import com.altix.ezpark.reservations.domain.model.commands.UpdateStatusCommand;
import com.altix.ezpark.reservations.domain.model.exceptions.*;
import com.altix.ezpark.reservations.domain.model.valueobject.ParkingId;
import com.altix.ezpark.reservations.domain.model.valueobject.Status;
import com.altix.ezpark.reservations.domain.services.ReservationCommandService;
import com.altix.ezpark.reservations.infrastructure.persistence.jpa.repositories.ReservationRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class ReservationCommandServiceImpl implements ReservationCommandService {
    private final ReservationRepository reservationRepository;
    private final ProfileContextFacade profileFacade;
    private final VehiclesContextFacade vehiclesContextFacade;
    private final ParkingContextFacade parkingContextFacade;
    private final ScheduleContextFacade scheduleContextFacade;
    private final NotificationContextFacade notificationContextFacade;

    @Transactional
    @Override
    public Optional<Reservation> handle(CreateReservationCommand command) {
        try {
            if (!profileFacade.checkProfileExistById(command.guestId()) || !profileFacade.checkProfileExistById(command.hostId())) {
                throw new ProfileNotFoundException();
            }
            if (!vehiclesContextFacade.checkVehicleExistsById(command.vehicleId())) {
                throw new VehicleNotFoundException();
            }
            if (!parkingContextFacade.checkParkingExistById(command.parkingId())) {
                throw new ParkingNotFoundException();
            }
            if (!scheduleContextFacade.isScheduleAvailable(command.scheduleId())) {
                throw new ScheduleConflictException();
            }
        } catch (Exception e) {
            throw new RuntimeException("Fallo al validar pre-condiciones con servicios externos.", e);
        }

        List<Status> blockedStatuses = List.of(Status.Approved, Status.InProgress, Status.Completed);
        if (reservationRepository.existsByOverlapWithStatus(
                new ParkingId(command.parkingId()),
                command.reservationDate(),
                command.startTime(),
                command.endTime(),
                blockedStatuses)) {
            throw new ReservationOverlapException();
        }

        var reservation = new Reservation(command);
        reservation.setStatus(Status.Pending);

        var savedReservation = reservationRepository.save(reservation);
        // Aquí podrías publicar un evento de dominio "ReservationCreatedEvent"
        // kafkaEventPublisher.publish(..., new ReservationCreatedEvent(...));
        return Optional.of(savedReservation);
    }

    @Transactional
    @Override
    public Optional<Reservation> handle(UpdateReservationCommand command) {
        var reservationToUpdate = reservationRepository.findById(command.reservationId())
                .orElseThrow(ReservationNotFoundException::new);

        Long currentScheduleId = reservationToUpdate.getScheduleId().scheduleId();
        if (!command.scheduleId().equals(currentScheduleId)) {
            try {
                if (!scheduleContextFacade.isScheduleAvailable(command.scheduleId())) {
                    throw new ScheduleConflictException();
                }
            } catch (ExternalServiceCommunicationException e) {
                throw new RuntimeException("Fallo al validar disponibilidad del nuevo horario: servicio externo no disponible.", e);
            } catch (Exception e) {
                throw new RuntimeException("Error inesperado al validar la disponibilidad del nuevo horario.", e);
            }
        }

        List<Status> blockedStatuses = List.of(Status.Approved, Status.InProgress, Status.Completed);
        if (reservationRepository.existsByOverlapWithStatus(
                new ParkingId(reservationToUpdate.getParkingId().parkingId()),
                command.reservationDate(),
                command.startTime(),
                command.endTime(),
                blockedStatuses)) {
            throw new ReservationOverlapException();
        }


        try {
            reservationToUpdate.updatedReservation(command);
            var updatedReservation = reservationRepository.save(reservationToUpdate);
            return Optional.of(updatedReservation);
        } catch (Exception e) {
            throw new ReservationUpdateException();
        }
    }

    @Override
    @Transactional
    public Optional<Reservation> handle(UpdateStatusCommand command) {
        var result = reservationRepository.findById(command.reservationId());
        if (result.isEmpty())
            throw new ReservationNotFoundException();
        var statusToUpdate = result.get();
        try {
            if (command.status() == Status.Approved) {
                scheduleContextFacade.markScheduleAsUnavailable(
                        statusToUpdate.getScheduleId().scheduleId());
            }
            notificationContextFacade.notifyReservationStatusChanged(
                    statusToUpdate.getId(),
                    statusToUpdate.getStatus().name(),
                    command.status().name(),
                    statusToUpdate.getGuestId().guestId(),
                    statusToUpdate.getHostId().hostId(),
                    statusToUpdate.getParkingId().parkingId(),
                    statusToUpdate.getScheduleId().scheduleId(),
                    statusToUpdate.getReservationDate()
            );
            var updatedStatus = reservationRepository.save(statusToUpdate.updatedStatus(command));
            return Optional.of(updatedStatus);
        }catch (Exception e){
            throw new ReservationUpdateException();
        }
    }
}
