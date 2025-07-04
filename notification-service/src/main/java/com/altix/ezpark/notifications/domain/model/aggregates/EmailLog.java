package com.altix.ezpark.notifications.domain.model.aggregates;

import com.altix.ezpark.notifications.domain.model.commands.CreateEmailLogCommand;
import com.altix.ezpark.notifications.domain.model.valueobjects.SendStatus;
import com.altix.ezpark.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "email_logs")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmailLog extends AuditableAbstractAggregateRoot<EmailLog> {

    @Column(nullable = false)
    private String recipient;

    @Column(nullable = false)
    private String subject;

    @Enumerated(EnumType.STRING)
    private SendStatus status;

    @Column(name = "reservation_id")
    private Long reservationId;

    @Column(name = "sent_at", nullable = false)
    private LocalDateTime sentAt;

    @Column(name = "error_message")
    private String errorMessage;

    public EmailLog(CreateEmailLogCommand command){
        this.recipient = command.recipient();
        this.subject = command.subject();
        this.status = command.status();
        this.reservationId = command.reservationId();
        this.sentAt = LocalDateTime.now();
        this.errorMessage = command.errorMessage();
    }
}
