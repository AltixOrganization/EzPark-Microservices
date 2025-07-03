package com.altix.ezpark.notifications.infrastructure.adapter.email;

import com.altix.ezpark.notifications.application.internal.outboundservices.NotificationSender;
import com.altix.ezpark.notifications.application.internal.outboundservices.acl.ParkingContextFacade;
import com.altix.ezpark.notifications.application.internal.outboundservices.acl.ProfileContextFacade;
import com.altix.ezpark.notifications.application.internal.outboundservices.EmailTemplateService;
import com.altix.ezpark.notifications.domain.model.valueobjects.UserRole;
import com.altix.ezpark.parkings.application.dtos.ParkingForNotificationResponse;
import com.altix.ezpark.profiles.application.dtos.ProfileResponse;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
@Slf4j
public class NotificationSenderImpl implements NotificationSender {

    private final ProfileContextFacade profileContextFacade;
    private final ParkingContextFacade parkingContextFacade;
    private final EmailTemplateService emailTemplateService;
    private final JavaMailSender javaMailSender;

    @Value("${notification.sender.email}")
    private String senderEmail;

    public NotificationSenderImpl(
            ProfileContextFacade profileContextFacade,
            ParkingContextFacade parkingContextFacade, EmailTemplateService emailTemplateService,
            JavaMailSender javaMailSender) {
        this.profileContextFacade = profileContextFacade;
        this.parkingContextFacade = parkingContextFacade;
        this.emailTemplateService = emailTemplateService;
        this.javaMailSender = javaMailSender;
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

            log.info("Email HTML enviado a {} ({}): {}",
                    recipient.email(), userRole, subject);

        } catch (Exception e) {
            log.error("Error enviando email HTML a {} ({}): {}",
                    recipient.email(), userRole, e.getMessage(), e);
        }
    }

    private String getGuestStatusMessage(String status, Long reservationId) {
        return switch (status.toUpperCase()) {
            case "CONFIRMED" -> String.format("🎉 ¡Excelente noticia! Tu reserva #%d ha sido **CONFIRMADA**.", reservationId);
            case "CANCELLED" -> String.format("😔 Tu reserva #%d ha sido **CANCELADA**.", reservationId);
            case "PENDING" -> String.format("⏳ Tu reserva #%d está **PENDIENTE** de confirmación.", reservationId);
            case "COMPLETED" -> String.format("✅ Tu reserva #%d se ha **COMPLETADO** exitosamente.", reservationId);
            default -> String.format("ℹ️ El estado de tu reserva #%d ha sido actualizado.", reservationId);
        };
    }

    private String getHostStatusMessage(String status, String guestName, Long reservationId) {
        return switch (status.toUpperCase()) {
            case "CONFIRMED" -> String.format("✅ Has **CONFIRMADO** la reserva de %s (Reserva #%d).", guestName, reservationId);
            case "CANCELLED" -> String.format("❌ La reserva de %s ha sido **CANCELADA** (Reserva #%d).", guestName, reservationId);
            case "PENDING" -> String.format("📬 **Nueva solicitud** de reserva de %s (Reserva #%d).", guestName, reservationId);
            case "COMPLETED" -> String.format("🏁 La reserva de %s se ha **COMPLETADO** (Reserva #%d).", guestName, reservationId);
            default -> String.format("📄 Actualización en la reserva de %s (Reserva #%d).", guestName, reservationId);
        };
    }

    private String getGuestActionMessage(String status, String hostName) {
        return switch (status.toUpperCase()) {
            case "CONFIRMED" -> String.format("🚗 Llega puntualmente y contacta a %s si tienes dudas.", hostName);
            case "CANCELLED" -> "🔍 Busca otros espacios disponibles en tu zona.";
            case "PENDING" -> String.format("⏰ %s revisará tu solicitud pronto.", hostName);
            case "COMPLETED" -> "⭐ ¡Nos encantaría conocer tu experiencia! Califica al propietario.";
            default -> "📞 Si tienes preguntas, contacta al propietario.";
        };
    }

    private String getHostActionMessage(String status, String guestName) {
        return switch (status.toUpperCase()) {
            case "CONFIRMED" -> String.format("🏠 Prepárate para recibir a %s. Asegúrate de que el espacio esté libre.", guestName);
            case "CANCELLED" -> "📈 Tu espacio está nuevamente disponible para otras reservas.";
            case "PENDING" -> "⚡ Revisa los detalles y confirma o rechaza según tu disponibilidad.";
            case "COMPLETED" -> "💰 ¡Reserva completada! El pago se procesará automáticamente.";
            default -> "📧 Mantente en contacto con el huésped si es necesario.";
        };
    }

    private String getStatusInSpanish(String status) {
        return switch (status.toUpperCase()) {
            case "CONFIRMED" -> "CONFIRMADA";
            case "CANCELLED" -> "CANCELADA";
            case "PENDING" -> "PENDIENTE";
            case "COMPLETED" -> "COMPLETADA";
            default -> status.toUpperCase();
        };
    }

    private String formatTime(java.time.LocalTime time) {
        return time != null ? time.format(DateTimeFormatter.ofPattern("HH:mm")) : "N/A";
    }
}