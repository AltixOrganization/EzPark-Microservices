package com.altix.ezpark.payments.interfaces.rest.transformers;


import com.altix.ezpark.payments.domain.model.aggregates.Payment;
import com.altix.ezpark.payments.interfaces.rest.resources.PaymentResource;

public class PaymentResourceFromEntityAssembler {
    public static PaymentResource toResourceFromEntity(Payment payment) {
        return new PaymentResource(
                payment.getId(),
                payment.getAmount(),
                payment.getCurrency().name(),
                payment.getPaymentDate(),
                payment.getStatus().name(),
                payment.getPaymentMethod().name(),
                payment.getReservationId().reservationId(),
                payment.getCreatedAt(),
                payment.getUpdatedAt()
        );
    }
}
