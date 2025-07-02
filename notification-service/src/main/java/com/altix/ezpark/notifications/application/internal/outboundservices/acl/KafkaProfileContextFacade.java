package com.altix.ezpark.notifications.application.internal.outboundservices.acl;

import com.altix.ezpark.profiles.application.dtos.ProfileResponse;
import com.altix.ezpark.profiles.application.dtos.ProfileValidationRequest;
import com.altix.ezpark.profiles.infrastructure.messaging.ProfileKafkaConfig;
import com.altix.ezpark.shared.infrastructure.messaging.KafkaEventPublisher;
import com.altix.ezpark.shared.infrastructure.messaging.ResponseRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.TimeoutException;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaProfileContextFacade implements ProfileContextFacade{
    private final KafkaEventPublisher eventPublisher;
    private final ResponseRegistry responseRegistry;

    @Override
    public ProfileResponse getProfileById(Long profileId) throws Exception{
        String correlationId = UUID.randomUUID().toString();

        ProfileValidationRequest request = new ProfileValidationRequest(correlationId, profileId);
        try {
            eventPublisher.publish(ProfileKafkaConfig.TOPIC_PROFILE_COMMANDS_REQUEST, request);

            ProfileResponse response = responseRegistry.waitForResponse(correlationId, ProfileResponse.class);
            log.info("Respuesta recibida para perfil {} (correlationId {}): email={}, nombre={}",
                    response.id(), response.correlationId(), response.email(), response.firstName());
            return response;
        } catch (TimeoutException e) {
            log.error("Timeout esperando la respuesta de validación para el perfil {} (correlationId {})",
                    profileId, correlationId, e);
            throw new Exception("Timeout al validar el perfil. El servicio de perfiles podría no estar disponible.", e);

        } catch (Exception e) {
            log.error("Error inesperado durante la validación del perfil {} (correlationId {})",
                    profileId, correlationId, e);
            throw new Exception("Error inesperado al comunicarse con el servicio de perfiles.", e);
        }

    }
}
