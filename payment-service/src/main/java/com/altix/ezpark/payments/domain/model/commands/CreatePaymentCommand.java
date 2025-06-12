package com.altix.ezpark.payments.domain.model.commands;

import java.math.BigDecimal;

public record CreatePaymentCommand(
         BigDecimal amount,
         String currency,
         String status,
         String paymentMethod,
         Long reservationId
         ) {
}
