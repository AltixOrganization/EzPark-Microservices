package com.altix.ezpark.payments.domain.service;

import com.altix.ezpark.payments.domain.model.aggregates.Payment;
import com.altix.ezpark.payments.domain.model.commands.CreatePaymentCommand;

import java.util.Optional;

public interface PaymentCommandService {
    Optional<Payment> handle(CreatePaymentCommand command);
}
