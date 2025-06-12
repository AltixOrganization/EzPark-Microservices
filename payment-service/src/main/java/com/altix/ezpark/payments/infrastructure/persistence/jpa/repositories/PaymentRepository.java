package com.altix.ezpark.payments.infrastructure.persistence.jpa.repositories;

import com.altix.ezpark.payments.domain.model.aggregates.Payment;
import com.altix.ezpark.payments.domain.model.valueobject.ReservationId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findByReservationId(ReservationId reservationId);
}
