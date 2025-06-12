package com.altix.ezpark.payments.domain.model.aggregates;

import com.altix.ezpark.payments.domain.model.commands.CreatePaymentCommand;
import com.altix.ezpark.payments.domain.model.valueobject.CurrencyType;
import com.altix.ezpark.payments.domain.model.valueobject.PaymentMethodType;
import com.altix.ezpark.payments.domain.model.valueobject.PaymentStatus;
import com.altix.ezpark.payments.domain.model.valueobject.ReservationId;
import com.altix.ezpark.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "payments")
public class Payment extends AuditableAbstractAggregateRoot<Payment> {
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    private CurrencyType currency;

    private LocalDateTime paymentDate;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;


    @Enumerated(EnumType.STRING)
    private PaymentMethodType paymentMethod;

    @Embedded
    private ReservationId reservationId;

    public Payment(CreatePaymentCommand command) {
        this.amount = command.amount();
        this.currency = CurrencyType.valueOf(command.currency());
        this.paymentDate = LocalDateTime.now();
        this.status = PaymentStatus.valueOf(command.status());
        this.paymentMethod = PaymentMethodType.valueOf(command.paymentMethod());
        this.reservationId = new ReservationId(command.reservationId());
    }
}
