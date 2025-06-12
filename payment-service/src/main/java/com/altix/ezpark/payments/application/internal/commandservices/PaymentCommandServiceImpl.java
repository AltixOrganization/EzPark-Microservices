package com.altix.ezpark.payments.application.internal.commandservices;

import com.altix.ezpark.payments.application.internal.outboundservices.acl.KafkaReservationContextFacade;
import com.altix.ezpark.payments.domain.model.aggregates.Payment;
import com.altix.ezpark.payments.domain.model.commands.CreatePaymentCommand;
import com.altix.ezpark.payments.domain.model.exceptions.ExternalServiceCommunicationException;
import com.altix.ezpark.payments.domain.model.exceptions.PaymentAlreadyExistsException;
import com.altix.ezpark.payments.domain.model.exceptions.ReservationApprovalException;
import com.altix.ezpark.payments.domain.model.valueobject.PaymentStatus;
import com.altix.ezpark.payments.domain.model.valueobject.ReservationId;
import com.altix.ezpark.payments.domain.service.PaymentCommandService;
import com.altix.ezpark.payments.infrastructure.persistence.jpa.repositories.PaymentRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.altix.ezpark.payments.domain.model.valueobject.PaymentStatus.*;

@AllArgsConstructor
@Service
public class PaymentCommandServiceImpl implements PaymentCommandService {
    private PaymentRepository paymentRepository;
    private final KafkaReservationContextFacade kafkaReservationContextFacade;

    @Transactional
    @Override
    public Optional<Payment> handle(CreatePaymentCommand command) {
        var reservationId = new ReservationId(command.reservationId());
        var paymentOptional = paymentRepository.findByReservationId(reservationId);
        if (paymentOptional.isPresent()) {
            throw new PaymentAlreadyExistsException();
        }
        try {
            switch (PaymentStatus.valueOf(command.status())) {
                case PENDING, FAILED -> throw new IllegalArgumentException("La reserva no existe o no está disponible para el pago.");
                case COMPLETED -> {
                    kafkaReservationContextFacade.approveReservation(command.reservationId());
                }
                default -> throw new IllegalArgumentException("Estado de pago desconocido: " + command.status());
            }

        } catch (ReservationApprovalException e) {

            throw e;

        } catch (Exception e) {
            throw new ExternalServiceCommunicationException("Fallo en la comunicación con el servicio de reservas durante la creación del pago.", e);
        }

        var payment = new Payment(command);
        var paymentSaved = paymentRepository.save(payment);

        return Optional.of(paymentSaved);
    }
}
