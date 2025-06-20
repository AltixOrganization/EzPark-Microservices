package com.altix.ezpark.reservations.interfaces.rest;

import com.altix.ezpark.reservations.domain.model.queries.*;
import com.altix.ezpark.reservations.domain.services.ReservationCommandService;
import com.altix.ezpark.reservations.domain.services.ReservationQueryService;
import com.altix.ezpark.reservations.interfaces.rest.resources.CreateReservationResource;
import com.altix.ezpark.reservations.interfaces.rest.resources.ReservationResource;
import com.altix.ezpark.reservations.interfaces.rest.resources.UpdateReservationResource;
import com.altix.ezpark.reservations.interfaces.rest.resources.UpdateStatusResource;
import com.altix.ezpark.reservations.interfaces.rest.transformers.CreateReservationCommandFromResourceAssembler;
import com.altix.ezpark.reservations.interfaces.rest.transformers.ReservationResourceFromEntityAssembler;
import com.altix.ezpark.reservations.interfaces.rest.transformers.UpdateReservationCommandFromResourceAssembler;
import com.altix.ezpark.reservations.interfaces.rest.transformers.UpdateStatusCommandFromResourceAssembler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "api/v1/reservations")
public class ReservationController {
    private final ReservationCommandService reservationCommandService;
    private final ReservationQueryService reservationQueryService;


    public ReservationController(ReservationCommandService reservationCommandService, ReservationQueryService reservationQueryService) {
        this.reservationCommandService = reservationCommandService;
        this.reservationQueryService = reservationQueryService;
    }

    @GetMapping
    public ResponseEntity<List<ReservationResource>> getAllReservations(){
     var getAllReservationsQuery = new GetAllReservationsQuery();
     var reservationList = reservationQueryService.handle(getAllReservationsQuery)
             .stream()
             .map(ReservationResourceFromEntityAssembler::toResourceFromEntity)
             .toList();
     return new ResponseEntity<>(reservationList,HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ReservationResource> createReservation(
            @RequestBody CreateReservationResource createReservationResource
            ) {

        var createReservationCommand = CreateReservationCommandFromResourceAssembler.toCommandFromResource(createReservationResource);

        var reservation = reservationCommandService.handle(createReservationCommand)
                .map(ReservationResourceFromEntityAssembler::toResourceFromEntity);

        return reservation.map(r -> new ResponseEntity<>(r, HttpStatus.CREATED))
                .orElse(new ResponseEntity<>(HttpStatus.BAD_REQUEST));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReservationResource> updateReservation(@PathVariable("id") Long id, @RequestBody UpdateReservationResource updateReservationResource) {
        var updateReservationCommand = UpdateReservationCommandFromResourceAssembler.toCommandFromResource(id, updateReservationResource);
        var updatedReservation = reservationCommandService.handle(updateReservationCommand)
                .map(ReservationResourceFromEntityAssembler::toResourceFromEntity);
        return updatedReservation.map(r -> new ResponseEntity<>(r, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    @PutMapping("/{id}/status")
    public ResponseEntity<ReservationResource> updateReservationStatus(@PathVariable("id") Long id, @RequestBody UpdateStatusResource updateStatusResource){
        var updateStatusCommand = UpdateStatusCommandFromResourceAssembler.toCommandFromResource(id, updateStatusResource);
        var updatedStatus = reservationCommandService.handle(updateStatusCommand).map(ReservationResourceFromEntityAssembler::toResourceFromEntity);
        return updatedStatus.map(r -> new ResponseEntity<>(r,HttpStatus.OK)).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReservationResource> getReservationById(@PathVariable("id") Long id) {
        var getReservationByIdQuery = new GetReservationByIdQuery(id);

        var reservation = reservationQueryService.handle(getReservationByIdQuery).map(ReservationResourceFromEntityAssembler::toResourceFromEntity);

        return reservation.map(r -> new ResponseEntity<>(r, HttpStatus.OK)).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    @GetMapping("/host/{hostId}")
    public ResponseEntity<List<ReservationResource>> getReservationsByHostId(@PathVariable("hostId") Long hostId){
        var getReservationsByHostIdQuery = new GetReservationsByHostIdQuery(hostId);
        var reservationList = reservationQueryService.handle(getReservationsByHostIdQuery)
                .stream()
                .map(ReservationResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return new ResponseEntity<>(reservationList,HttpStatus.OK);
    }
    @GetMapping("/guest/{guestId}")
    public ResponseEntity<List<ReservationResource>> getReservationsByGuestId(@PathVariable("guestId") Long guestId){
        var getReservationsByGuestIdQuery = new GetReservationsByGuestIdQuery(guestId);
        var reservationList = reservationQueryService.handle(getReservationsByGuestIdQuery)
                .stream()
                .map(ReservationResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return new ResponseEntity<>(reservationList,HttpStatus.OK);
    }

    @GetMapping("/inProgress")
    public ResponseEntity<List<ReservationResource>> getInProgressReservation(){
        var getInProgressReservationQuery = new GetInProgressReservationQuery();
        var inProgressList = reservationQueryService.handle(getInProgressReservationQuery)
                .stream()
                .map(ReservationResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return new ResponseEntity<>(inProgressList,HttpStatus.OK);
    }
    @GetMapping("/upComing")
    public ResponseEntity<List<ReservationResource>> getUpComingReservation(){
        var getUpComingReservationQuery = new GetUpComingReservationQuery();
        var upComingList = reservationQueryService.handle(getUpComingReservationQuery)
                .stream()
                .map(ReservationResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return new ResponseEntity<>(upComingList,HttpStatus.OK);
    }
    @GetMapping("/past")
    public ResponseEntity<List<ReservationResource>> getPastReservations(){
        var getPastReservationQuery = new GetPastReservationQuery();
        var pastList = reservationQueryService.handle(getPastReservationQuery)
                .stream()
                .map(ReservationResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return new ResponseEntity<>(pastList,HttpStatus.OK);
    }
}
