package com.altix.ezpark.payments.interfaces.rest.resources;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PaymentResource(
        Long id,
        BigDecimal amount,
        String currency,
        LocalDateTime paymentDate,
        String status,
        String paymentMethod,
        Long reservationId,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
