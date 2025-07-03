package com.altix.ezpark.notifications.infrastructure.adapter.email;

import com.altix.ezpark.notifications.application.internal.outboundservices.EmailTemplateService;
import com.altix.ezpark.notifications.domain.model.valueobjects.UserRole;
import com.altix.ezpark.parkings.application.dtos.ParkingForNotificationResponse;
import com.altix.ezpark.profiles.application.dtos.ProfileResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailTemplateServiceImpl implements EmailTemplateService {
    private final TemplateEngine templateEngine;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd 'de' MMMM 'de' yyyy");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    @Override
    public String buildEmailContent(
            UserRole userRole,
            String previousStatus,
            String newStatus,
            ProfileResponse recipient,
            ProfileResponse otherParty,
            ParkingForNotificationResponse parkingInfo,
            LocalDate reservationDate,
            Long reservationId
    ) {
        Context context = new Context();

        setupCommonData(context, parkingInfo, reservationDate, reservationId, newStatus);

        return switch (userRole) {
            case GUEST -> {
                setupGuestData(context, newStatus, recipient, otherParty, reservationId);
                yield templateEngine.process("email/guest-notification", context);
            }
            case HOST -> {
                setupHostData(context, newStatus, recipient, otherParty, reservationId);
                yield templateEngine.process("email/host-notification", context);
            }
        };
    }

    @Override
    public String getSubject(UserRole userRole, String newStatus, String location) {
        return switch (userRole) {
            case GUEST -> String.format("🅿️ Tu reserva está %s - Parking en %s",
                    getStatusInSpanish(newStatus), location);
            case HOST -> String.format("🏠 Reserva %s en tu parking - %s",
                    getStatusInSpanish(newStatus), location);
        };
    }

    private void setupCommonData(Context context, ParkingForNotificationResponse parkingInfo,
                                 LocalDate reservationDate, Long reservationId, String newStatus) {
        context.setVariable("reservationId", reservationId);
        context.setVariable("reservationDate", reservationDate.format(DATE_FORMATTER));
        context.setVariable("startTime", formatTime(parkingInfo.startTime()));
        context.setVariable("endTime", formatTime(parkingInfo.endTime()));
        context.setVariable("address", parkingInfo.address());
        context.setVariable("numDirection", parkingInfo.numDirection() != null ? parkingInfo.numDirection() : "");
        context.setVariable("district", parkingInfo.district());
        context.setVariable("city", parkingInfo.city());
        context.setVariable("phone", parkingInfo.phone() != null ? parkingInfo.phone() : "No disponible");
        context.setVariable("statusText", getStatusInSpanish(newStatus));
        context.setVariable("statusClass", newStatus.toLowerCase());
    }

    private void setupGuestData(Context context, String newStatus, ProfileResponse guest, ProfileResponse host, Long reservationId) {
        context.setVariable("guestName", guest.firstName());
        context.setVariable("hostFirstName", host.firstName());
        context.setVariable("hostLastName", host.lastName());
        context.setVariable("statusMessage", getGuestStatusMessage(newStatus, reservationId));
        context.setVariable("actionTitle", getGuestActionTitle(newStatus));
        context.setVariable("actionMessages", getGuestActionMessages(newStatus, host.firstName()));
        context.setVariable("footerTip", getGuestFooterTip(newStatus));
    }

    private void setupHostData(Context context, String newStatus, ProfileResponse host, ProfileResponse guest, Long reservationId) {
        context.setVariable("hostName", host.firstName());
        context.setVariable("guestFirstName", guest.firstName());
        context.setVariable("guestLastName", guest.lastName());
        context.setVariable("guestEmail", guest.email());
        context.setVariable("statusMessage", getHostStatusMessage(newStatus, guest.firstName(), reservationId));
        context.setVariable("actionTitle", getHostActionTitle(newStatus));
        context.setVariable("actionMessages", getHostActionMessages(newStatus, guest.firstName()));
        context.setVariable("footerTip", getHostFooterTip(newStatus));
        context.setVariable("showEarnings", "COMPLETED".equalsIgnoreCase(newStatus));
    }


    private String getGuestStatusMessage(String status, Long reservationId) {
        return switch (status.toUpperCase()) {
            case "CONFIRMED" -> String.format("🎉 ¡Excelente noticia! Tu reserva #%d ha sido confirmada.", reservationId);
            case "CANCELLED" -> String.format("😔 Tu reserva #%d ha sido cancelada.", reservationId);
            case "PENDING" -> String.format("⏳ Tu reserva #%d está pendiente de confirmación.", reservationId);
            case "COMPLETED" -> String.format("✅ Tu reserva #%d se ha completado exitosamente.", reservationId);
            default -> String.format("ℹ️ El estado de tu reserva #%d ha sido actualizado.", reservationId);
        };
    }

    private String getHostStatusMessage(String status, String guestName, Long reservationId) {
        return switch (status.toUpperCase()) {
            case "CONFIRMED" -> String.format("✅ Has confirmado la reserva de %s (Reserva #%d).", guestName, reservationId);
            case "CANCELLED" -> String.format("❌ La reserva de %s ha sido cancelada (Reserva #%d).", guestName, reservationId);
            case "PENDING" -> String.format("📬 Nueva solicitud de reserva de %s (Reserva #%d).", guestName, reservationId);
            case "COMPLETED" -> String.format("🏁 La reserva de %s se ha completado (Reserva #%d).", guestName, reservationId);
            default -> String.format("📄 Actualización en la reserva de %s (Reserva #%d).", guestName, reservationId);
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
        return time != null ? time.format(TIME_FORMATTER) : "N/A";
    }

    private String getGuestActionTitle(String status) {
        return switch (status.toUpperCase()) {
            case "CONFIRMED" -> "🚗 ¿Qué sigue?";
            case "CANCELLED" -> "🔍 ¿Necesitas otro parking?";
            case "PENDING" -> "⏰ Mientras esperas...";
            case "COMPLETED" -> "⭐ ¡Nos encantaría conocer tu experiencia!";
            default -> "📞 Información adicional";
        };
    }

    private List<String> getGuestActionMessages(String status, String hostName) {
        return switch (status.toUpperCase()) {
            case "CONFIRMED" -> Arrays.asList(
                    "Llega puntualmente a la hora acordada",
                    String.format("Si tienes dudas, contacta a %s", hostName),
                    "Recuerda dejar el espacio limpio"
            );
            case "CANCELLED" -> Arrays.asList(
                    "Busca espacios disponibles en tu zona",
                    "Filtra por fecha y horario",
                    "¡Reserva con confianza!"
            );
            case "PENDING" -> Arrays.asList(
                    String.format("%s revisará tu solicitud pronto", hostName),
                    "Te notificaremos cuando responda",
                    "Mantén tu teléfono disponible"
            );
            case "COMPLETED" -> Arrays.asList(
                    "Califica al propietario",
                    "Comparte tu reseña",
                    "Ayuda a otros usuarios"
            );
            default -> Arrays.asList("Si tienes preguntas, contacta al propietario");
        };
    }

    private String getGuestFooterTip(String status) {
        return switch (status.toUpperCase()) {
            case "CONFIRMED" -> "🎯 Tip: Llega 5 minutos antes para ubicar fácilmente el espacio.";
            case "CANCELLED" -> "💡 Tip: Reserva con antelación para mayor disponibilidad.";
            case "PENDING" -> "🕐 Tip: Las confirmaciones suelen llegar en menos de 2 horas.";
            case "COMPLETED" -> "🎁 Tip: Los usuarios frecuentes obtienen descuentos especiales.";
            default -> "💙 Gracias por usar EzPark.";
        };
    }

    private String getHostActionTitle(String status) {
        return switch (status.toUpperCase()) {
            case "CONFIRMED" -> "🏠 Prepárate para recibir al huésped:";
            case "CANCELLED" -> "📈 Tu espacio está nuevamente disponible:";
            case "PENDING" -> "⚡ Acción requerida:";
            case "COMPLETED" -> "💰 ¡Reserva completada exitosamente!";
            default -> "📧 Información adicional";
        };
    }

    private List<String> getHostActionMessages(String status, String guestName) {
        return switch (status.toUpperCase()) {
            case "CONFIRMED" -> Arrays.asList(
                    "Asegúrate de que el espacio esté libre",
                    "Ten tu teléfono disponible",
                    "Proporciona indicaciones claras si es necesario"
            );
            case "CANCELLED" -> Arrays.asList(
                    "Aparecerá en búsquedas automáticamente",
                    "Revisa tu calendario de disponibilidad",
                    "Considera ajustar horarios si es necesario"
            );
            case "PENDING" -> Arrays.asList(
                    "Revisa los detalles de la solicitud",
                    "Confirma o rechaza según tu disponibilidad",
                    "Responde pronto para mejor experiencia del usuario"
            );
            case "COMPLETED" -> Arrays.asList(
                    "El pago se procesará automáticamente",
                    "Revisa tus ganancias en el dashboard",
                    "¡Esperamos más reservas pronto!"
            );
            default -> Arrays.asList("Mantente en contacto con el huésped si es necesario");
        };
    }

    private String getHostFooterTip(String status) {
        return switch (status.toUpperCase()) {
            case "CONFIRMED" -> "🌟 Tip: Una buena comunicación genera mejores reseñas.";
            case "CANCELLED" -> "📊 Tip: Analiza patrones de cancelación para optimizar precios.";
            case "PENDING" -> "⚡ Tip: Respuestas rápidas aumentan tu rating como anfitrión.";
            case "COMPLETED" -> "🎯 Tip: Solicita reseñas para atraer más huéspedes.";
            default -> "🏠 Gracias por ser parte de la comunidad EzPark.";
        };
    }
}
