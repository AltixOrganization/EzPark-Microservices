package com.altix.ezpark.reviews.application.internal.outboundservices.acl;

import com.altix.ezpark.profiles.application.dtos.ProfileValidationRequest;
import com.altix.ezpark.profiles.application.dtos.ProfileValidationResponse;
import com.altix.ezpark.profiles.infrastructure.messaging.ProfileKafkaConfig;
import com.altix.ezpark.shared.infrastructure.messaging.KafkaEventPublisher;
import com.altix.ezpark.shared.infrastructure.messaging.ResponseRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.TimeoutException;

@Service("ReservationsProfileContextFacade")
@RequiredArgsConstructor
@Slf4j
public class KafkaProfileContextFacade implements ProfileContextFacade {

    private final KafkaEventPublisher eventPublisher;
    private final ResponseRegistry responseRegistry;

    @Override
    public boolean checkProfileExistById(Long userId) throws Exception {
        String correlationId = UUID.randomUUID().toString();

        ProfileValidationRequest request = new ProfileValidationRequest(correlationId, userId);

        try {
            log.info("Enviando solicitud de validación para perfil {} con correlationId {}",
                    request.profileId(), request.correlationId());

            eventPublisher.publish(ProfileKafkaConfig.TOPIC_PROFILE_VALIDATION_REQUEST, request);

            ProfileValidationResponse response = responseRegistry.waitForResponse(correlationId, ProfileValidationResponse.class);

            log.info("Respuesta de validación recibida para perfil {} (correlationId {}): existe={}",
                    response.profileId(), response.correlationId(), response.exists());

            return response.exists();

        } catch (TimeoutException e) {
            log.error("Timeout esperando la respuesta de validación para el perfil {} (correlationId {})",
                    userId, correlationId, e);
            throw new Exception("Timeout al validar el perfil. El servicio de perfiles podría no estar disponible.", e);

        } catch (Exception e) {
            log.error("Error inesperado durante la validación del perfil {} (correlationId {})",
                    userId, correlationId, e);
            throw new Exception("Error inesperado al comunicarse con el servicio de perfiles.", e);
        }
    }
}
