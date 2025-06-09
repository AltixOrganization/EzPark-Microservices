package com.altix.ezpark.payments.interfaces.rest.resources;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record CreatePaymentResource(
        @NotNull(message = "El monto no puede ser nulo")
        @Positive(message = "El monto debe ser mayor a cero")
        @Digits(integer = 8, fraction = 2, message = "El monto debe tener máximo 8 dígitos enteros y 2 decimales")
        BigDecimal amount,

        @NotBlank(message = "La moneda no puede estar vacía")
        @Pattern(regexp = "USD|PEN|EUR", message = "Moneda no válida. Valores permitidos: USD, PEN, EUR")
        String currency,

        @NotBlank(message = "El estado no puede estar vacío")
        @Pattern(regexp = "PENDING|COMPLETED|FAILED|REFUNDED|CANCELED",
                message = "Estado no válido. Valores permitidos: PENDING, COMPLETED, FAILED, REFUNDED, CANCELED")
        String status,

        @NotBlank(message = "El método de pago no puede estar vacío")
        @Pattern(regexp = "CREDIT_CARD|DEBIT_CARD|PAYPAL|CASH|BANK_TRANSFER",
                message = "Método de pago no válido. Valores permitidos: CREDIT_CARD, DEBIT_CARD, PAYPAL, CASH, BANK_TRANSFER")
        String paymentMethod,

        @NotNull(message = "El ID de reserva no puede ser nulo")
        @Positive(message = "El ID de reserva debe ser un número positivo")
        Long reservationId
) {
}
