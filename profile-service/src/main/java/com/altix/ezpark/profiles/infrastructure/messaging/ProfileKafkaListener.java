package com.altix.ezpark.profiles.infrastructure.messaging;


import com.altix.ezpark.iam.application.dtos.UserSignedUpEvent;
import com.altix.ezpark.iam.infrastructure.messaging.IamKafkaConfig;
import com.altix.ezpark.profiles.application.dtos.ProfileValidationRequest;
import com.altix.ezpark.profiles.application.dtos.ProfileValidationResponse;
import com.altix.ezpark.profiles.domain.model.commands.CreateProfileCommand;
import com.altix.ezpark.profiles.domain.model.queries.GetProfileByIdQuery;
import com.altix.ezpark.profiles.domain.services.ProfileCommandService;
import com.altix.ezpark.profiles.domain.services.ProfileQueryService;
import com.altix.ezpark.profiles.infrastructure.persistence.jpa.repositories.ProfileRepository;
import com.altix.ezpark.shared.infrastructure.messaging.KafkaEventPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ProfileKafkaListener {
    private final ProfileRepository profileRepository;
    private final ProfileQueryService profileQueryService;
    private final ProfileCommandService profileCommandService;
    private final KafkaEventPublisher kafkaEventPublisher;

    @KafkaListener(
            topics = ProfileKafkaConfig.TOPIC_PROFILE_VALIDATION_REQUEST,
            groupId = "${profiles.kafka.consumer.group-id:profile-group}"
    )
    public void handleProfileValidationRequest(ProfileValidationRequest request) {
        try {
            log.info("Recibida solicitud de validación para perfil {}", request.profileId());
            var getProfileByIdQuery = new GetProfileByIdQuery(request.profileId());

            boolean exists = profileQueryService.handle(getProfileByIdQuery).isPresent();

            ProfileValidationResponse response = new ProfileValidationResponse(
                    request.correlationId(),
                    request.profileId(),
                    exists
            );

            // Publicar la respuesta directamente.
            kafkaEventPublisher.publish(ProfileKafkaConfig.TOPIC_PROFILE_VALIDATION_RESPONSE, response);
            log.info("Enviada respuesta de validación para perfil {}: existe={}", request.profileId(), exists);

        } catch (Exception e) {
            log.error("Error procesando solicitud de validación de perfil para correlationId {}: {}", request.correlationId(), e.getMessage(), e);
        }
    }

    @KafkaListener(
            topics = IamKafkaConfig.TOPIC_USER_SIGNED_UP,
            groupId = "${profiles.kafka.consumer.signup-group-id:profile-creation-group}"
    )
    public void handleUserSignedUpEvent(UserSignedUpEvent event) {
        log.info("[Saga] Recibido UserSignedUpEvent para crear perfil para userId: {}", event.userId());

        try {
            // IDEMPOTENCIA: No crear un perfil si ya existe para este userId.
            if (profileRepository.existsByUserId_UserId(event.userId())) {
                log.warn("[Saga] Ya existe un perfil para el userId {}. Ignorando evento duplicado.", event.userId());
                return;
            }

            // Traducir el evento a un comando de dominio local.
            var createProfileCommand = new CreateProfileCommand(
                    event.firstName(),
                    event.lastName(),
                    event.birthDate(),
                    event.userId()
            );

            // Delegar la creación del perfil al servicio de negocio.
            profileCommandService.handle(createProfileCommand);

            log.info("[Saga] Perfil creado exitosamente para userId {}.", event.userId());

            // Nota: Aquí NO publicamos un evento de vuelta a IAM, según nuestro diseño simplificado.
            // Pero sí podríamos publicar un evento genérico TOPIC_PROFILE_CREATED si otros servicios lo necesitan.

        } catch (Exception e) {
            log.error("[Saga] Fallo CRÍTICO al crear el perfil para userId {}. Causa: {}",
                    event.userId(), e.getMessage(), e);
        }
    }
}
