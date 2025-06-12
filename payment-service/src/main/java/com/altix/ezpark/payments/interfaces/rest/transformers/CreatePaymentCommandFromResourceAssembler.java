package com.altix.ezpark.payments.interfaces.rest.transformers;

import com.altix.ezpark.payments.domain.model.commands.CreatePaymentCommand;
import com.altix.ezpark.payments.interfaces.rest.resources.CreatePaymentResource;

public class CreatePaymentCommandFromResourceAssembler {
    public static CreatePaymentCommand toCommandFromResource(CreatePaymentResource resource) {
        return new CreatePaymentCommand(
                resource.amount(),
                resource.currency(),
                resource.status(),
                resource.paymentMethod(),
                resource.reservationId()
        );
    }
}
