package com.altix.ezpark.notifications.infrastructure.adapter.email;

import com.altix.ezpark.notifications.application.internal.outboundservices.NotificationSender;
import com.altix.ezpark.notifications.application.internal.outboundservices.acl.ParkingContextFacade;
import com.altix.ezpark.notifications.application.internal.outboundservices.acl.ProfileContextFacade;
import com.altix.ezpark.notifications.application.internal.outboundservices.EmailTemplateService;
import com.altix.ezpark.notifications.domain.model.commands.CreateEmailLogCommand;
import com.altix.ezpark.notifications.domain.model.valueobjects.SendStatus;
import com.altix.ezpark.notifications.domain.model.valueobjects.UserRole;
import com.altix.ezpark.notifications.domain.service.EmailLogCommandService;
import com.altix.ezpark.parkings.application.dtos.ParkingForNotificationResponse;
import com.altix.ezpark.profiles.application.dtos.ProfileResponse;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@Slf4j
public class NotificationSenderImpl implements NotificationSender {

    private final ProfileContextFacade profileContextFacade;
    private final ParkingContextFacade parkingContextFacade;
    private final EmailTemplateService emailTemplateService;
    private final JavaMailSender javaMailSender;
    private final EmailLogCommandService emailLogCommandService;

    @Value("${notification.sender.email}")
    private String senderEmail;

    public NotificationSenderImpl(
            ProfileContextFacade profileContextFacade,
            ParkingContextFacade parkingContextFacade, EmailTemplateService emailTemplateService,
            JavaMailSender javaMailSender, EmailLogCommandService emailLogCommandService) {
        this.profileContextFacade = profileContextFacade;
        this.parkingContextFacade = parkingContextFacade;
        this.emailTemplateService = emailTemplateService;
        this.javaMailSender = javaMailSender;
        this.emailLogCommandService = emailLogCommandService;
    }

    @Override
    public void send(
            String previousStatus,
            String newStatus,
            Long guestId,
            Long hostId,
            Long parkingId,
            Long scheduleId,
            Long reservationId,
            LocalDate reservationDate
    ) {
        try {
            log.info("Enviando notificaciones para reserva {} - cambio de {} a {}",
                    reservationId, previousStatus, newStatus);

            var profile1 = profileContextFacade.getProfileById(guestId);
            var profile2 = profileContextFacade.getProfileById(hostId);

            var parkingInfo = parkingContextFacade.getParkingForNotification(
                    parkingId, reservationId, scheduleId
            );

            ProfileResponse actualHost, actualGuest;

            if (parkingInfo.parkingProfileId().equals(profile1.id())) {
                actualHost = profile1;
                actualGuest = profile2;
                log.info("Roles corregidos para reserva {}: profile1 (ID:{}) es el HOST real, profile2 (ID:{}) es el GUEST real",
                        reservationId, profile1.id(), profile2.id());
            } else if (parkingInfo.parkingProfileId().equals(profile2.id())) {
                actualHost = profile2;
                actualGuest = profile1;
                log.info("Roles normales para reserva {}: profile2 (ID:{}) es el HOST, profile1 (ID:{}) es el GUEST",
                        reservationId, profile2.id(), profile1.id());
            } else {
                log.error("INCONSISTENCIA en reserva {}: Ninguno de los perfiles ({}, {}) coincide con el dueño del parking ({})",
                        reservationId, guestId, hostId, parkingInfo.parkingProfileId());
                actualHost = profile2;
                actualGuest = profile1;
            }

            sendEmailToUser(UserRole.GUEST, actualGuest, actualHost, parkingInfo,
                    previousStatus, newStatus, reservationDate, reservationId);

            sendEmailToUser(UserRole.HOST, actualHost, actualGuest, parkingInfo,
                    previousStatus, newStatus, reservationDate, reservationId);

            log.info("Notificaciones enviadas exitosamente para reserva {} a GUEST: {} y HOST: {}",
                    reservationId, actualGuest.email(), actualHost.email());

        } catch (Exception e) {
            log.error("Error enviando notificaciones para reserva {}: {}",
                    reservationId, e.getMessage(), e);
        }
    }

    private void sendEmailToUser(
            UserRole userRole,
            ProfileResponse recipient,
            ProfileResponse otherParty,
            ParkingForNotificationResponse parkingInfo,
            String previousStatus,
            String newStatus,
            LocalDate reservationDate,
            Long reservationId
    ) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(senderEmail);
            helper.setTo(recipient.email());

            String subject = emailTemplateService.getSubject(
                    userRole, newStatus, parkingInfo.district()
            );
            helper.setSubject(subject);

            String htmlContent = emailTemplateService.buildEmailContent(
                    userRole,
                    previousStatus,
                    newStatus,
                    recipient,
                    otherParty,
                    parkingInfo,
                    reservationDate,
                    reservationId
            );

            helper.setText(htmlContent, true);
            javaMailSender.send(message);
            logEmailSent(recipient.email(), subject, reservationId);

            log.info("Email HTML enviado a {} ({}): {}",
                    recipient.email(), userRole, subject);

        } catch (Exception e) {
            log.error("Error enviando email HTML a {} ({}): {}",
                    recipient.email(), userRole, e.getMessage(), e);
            logEmailFailed(recipient.email(), emailTemplateService.getSubject(userRole, newStatus, parkingInfo.district()),
                    reservationId, e.getMessage());
        }
    }

    private void logEmailSent(String recipient, String subject, Long reservationId) {
        CreateEmailLogCommand command = new CreateEmailLogCommand(
                recipient, subject, SendStatus.SENT, reservationId, null
        );
        emailLogCommandService.handle(command);
    }

    private void logEmailFailed(String recipient, String subject,
                                Long reservationId, String errorMessage) {
        CreateEmailLogCommand command = new CreateEmailLogCommand(
                recipient, subject, SendStatus.FAILED, reservationId, errorMessage
        );
        emailLogCommandService.handle(command);
    }
}