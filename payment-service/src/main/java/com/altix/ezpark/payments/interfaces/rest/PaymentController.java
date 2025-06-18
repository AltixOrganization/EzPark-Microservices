package com.altix.ezpark.payments.interfaces.rest;

import com.altix.ezpark.payments.domain.model.queries.GetAllPaymentsQuery;
import com.altix.ezpark.payments.domain.model.queries.GetPaymentByReservationId;
import com.altix.ezpark.payments.domain.service.PaymentCommandService;
import com.altix.ezpark.payments.domain.service.PaymentQueryService;
import com.altix.ezpark.payments.interfaces.rest.resources.CreatePaymentResource;
import com.altix.ezpark.payments.interfaces.rest.resources.PaymentResource;
import com.altix.ezpark.payments.interfaces.rest.transformers.CreatePaymentCommandFromResourceAssembler;
import com.altix.ezpark.payments.interfaces.rest.transformers.PaymentResourceFromEntityAssembler;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping(value = "api/v1/payments")
public class PaymentController {
    private PaymentCommandService paymentCommandService;
    private PaymentQueryService paymentQueryService;

    @GetMapping()
    public ResponseEntity<List<PaymentResource>> getAllPayments() {
        var getAllPaymentsQuery = new GetAllPaymentsQuery();
        var resources = paymentQueryService.handle(getAllPaymentsQuery)
                .stream()
                .map(PaymentResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return ResponseEntity.ok(resources);
    }

    @GetMapping(params = {"reservationId"})
    public ResponseEntity<PaymentResource> getPaymentByReservationId(@RequestParam ("reservationId") Long reservationId) {
        var getPaymentByReservationIdQuery = new GetPaymentByReservationId(reservationId);
        var payment = paymentQueryService.handle(getPaymentByReservationIdQuery)
                .map(PaymentResourceFromEntityAssembler::toResourceFromEntity);
        return payment.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<PaymentResource> createPayment(@Valid @RequestBody CreatePaymentResource resource) {
        var payment = paymentCommandService.handle(CreatePaymentCommandFromResourceAssembler.toCommandFromResource(resource));
        var paymentResource = payment.map(PaymentResourceFromEntityAssembler::toResourceFromEntity);
        return paymentResource.map(ResponseEntity::ok)
                .orElse(ResponseEntity.badRequest().build());
    }
}
