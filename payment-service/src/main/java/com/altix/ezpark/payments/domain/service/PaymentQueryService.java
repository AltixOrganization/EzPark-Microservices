package com.altix.ezpark.payments.domain.service;

import com.altix.ezpark.payments.domain.model.aggregates.Payment;
import com.altix.ezpark.payments.domain.model.queries.GetAllPaymentsQuery;
import com.altix.ezpark.payments.domain.model.queries.GetPaymentByReservationId;

import java.util.List;
import java.util.Optional;

public interface PaymentQueryService {
    Optional<Payment> handle(GetPaymentByReservationId query);
    List<Payment> handle(GetAllPaymentsQuery query);
}
