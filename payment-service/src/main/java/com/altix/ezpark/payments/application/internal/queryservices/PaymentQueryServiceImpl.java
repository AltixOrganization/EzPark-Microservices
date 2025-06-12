package com.altix.ezpark.payments.application.internal.queryservices;

import com.altix.ezpark.payments.domain.model.aggregates.Payment;
import com.altix.ezpark.payments.domain.model.queries.GetAllPaymentsQuery;
import com.altix.ezpark.payments.domain.model.queries.GetPaymentByReservationId;
import com.altix.ezpark.payments.domain.model.valueobject.ReservationId;
import com.altix.ezpark.payments.domain.service.PaymentQueryService;
import com.altix.ezpark.payments.infrastructure.persistence.jpa.repositories.PaymentRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class PaymentQueryServiceImpl implements PaymentQueryService {
    private final PaymentRepository paymentRepository;

    @Override
    public Optional<Payment> handle(GetPaymentByReservationId query) {
        return paymentRepository.findByReservationId(new ReservationId(query.reservationId()));
    }

    @Override
    public List<Payment> handle(GetAllPaymentsQuery query) {
        return paymentRepository.findAll();
    }
}
