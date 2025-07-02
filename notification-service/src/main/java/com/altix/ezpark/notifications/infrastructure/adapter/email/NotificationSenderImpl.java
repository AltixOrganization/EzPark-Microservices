package com.altix.ezpark.notifications.infrastructure.adapter.email;

import com.altix.ezpark.notifications.application.internal.outboundservices.NotificationSender;
import com.altix.ezpark.notifications.application.internal.outboundservices.acl.ParkingContextFacade;
import com.altix.ezpark.notifications.application.internal.outboundservices.acl.ProfileContextFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
public class NotificationSenderImpl implements NotificationSender {
    private static final Logger log = LoggerFactory.getLogger(NotificationSenderImpl.class);

    private final ProfileContextFacade profileContextFacade;
    private final ParkingContextFacade parkingContextFacade;
    private final JavaMailSender javaMailSender;

    @Value("${notification.sender.email}")
    private String senderEmail;

    public NotificationSenderImpl(ProfileContextFacade profileContextFacade, ParkingContextFacade parkingContextFacade, JavaMailSender javaMailSender) {
        this.profileContextFacade = profileContextFacade;
        this.parkingContextFacade = parkingContextFacade;
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
            log.info("Enviando notificación para reserva {} - cambio de {} a {}",
                    reservationId, previousStatus, newStatus);

            // Obtener información en paralelo (podrías usar CompletableFuture para optimizar)
            var guestProfile = profileContextFacade.getProfileById(guestId);
            var hostProfile = profileContextFacade.getProfileById(hostId);
            var parkingInfo = parkingContextFacade.getParkingForNotification(
                    parkingId, reservationId, scheduleId
            );

            // Crear y enviar email
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(senderEmail);
            message.setTo(guestProfile.email());
            message.setSubject("Cambio de estado de reserva - Parking en " + parkingInfo.district());

            String emailContent = buildEmailContent(
                    guestProfile.firstName(),
                    hostProfile.firstName(),
                    previousStatus,
                    newStatus,
                    parkingInfo,
                    reservationDate,
                    reservationId
            );

            message.setText(emailContent);
            javaMailSender.send(message);

            log.info("Notificación enviada exitosamente para reserva {} al email {}",
                    reservationId, guestProfile.email());

        } catch (Exception e) {
            log.error("Error enviando notificación para reserva {}: {}", reservationId, e.getMessage(), e);
            // Aquí podrías implementar retry logic o enviar a un DLQ (Dead Letter Queue)
        }
    }

    private String buildEmailContent(
            String guestName,
            String hostName,
            String previousStatus,
            String newStatus,
            com.altix.ezpark.parkings.application.dtos.ParkingForNotificationResponse parkingInfo,
            LocalDate reservationDate,
            Long reservationId
    ) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        return String.format("""
            Hola %s,
            
            Tu reserva #%d ha cambiado de estado de "%s" a "%s".
            
            📍 DETALLES DE LA RESERVA:
            • Ubicación: %s %s
            • Distrito: %s, %s
            • Fecha: %s
            • Horario: %s - %s
            • Teléfono de contacto: %s
            • Propietario: %s
            
            %s
            
            ¡Gracias por usar EzPark!
            
            ---
            Este es un email automático, por favor no responder.
            """,
                guestName,
                reservationId,
                previousStatus,
                newStatus,
                parkingInfo.address(),
                parkingInfo.numDirection() != null ? parkingInfo.numDirection() : "",
                parkingInfo.district(),
                parkingInfo.city(),
                reservationDate.format(dateFormatter),
                parkingInfo.startTime() != null ? parkingInfo.startTime().format(timeFormatter) : "N/A",
                parkingInfo.endTime() != null ? parkingInfo.endTime().format(timeFormatter) : "N/A",
                parkingInfo.phone() != null ? parkingInfo.phone() : "No disponible",
                hostName,
                getStatusMessage(newStatus)
        );
    }

    private String getStatusMessage(String status) {
        return switch (status.toUpperCase()) {
            case "CONFIRMED" -> "✅ Tu reserva ha sido confirmada. ¡Disfruta tu estacionamiento!";
            case "CANCELLED" -> "❌ Tu reserva ha sido cancelada. Si tienes dudas, contacta al propietario.";
            case "PENDING" -> "⏳ Tu reserva está pendiente de confirmación.";
            case "COMPLETED" -> "🎉 Tu reserva se ha completado exitosamente. ¡Gracias por usar EzPark!";
            default -> "ℹ️ El estado de tu reserva ha sido actualizado.";
        };
    }
}
