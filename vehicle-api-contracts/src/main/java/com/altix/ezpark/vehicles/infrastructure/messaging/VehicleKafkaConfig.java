package com.altix.ezpark.vehicles.infrastructure.messaging;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class VehicleKafkaConfig {
    public static final String TOPIC_VEHICLE_VALIDATION_REQUEST = "vehicles.validation.request";
    public static final String TOPIC_VEHICLE_VALIDATION_RESPONSE = "vehicles.validation.response";

    public static final String TOPIC_VEHICLE_CREATED = "vehicles.created";
    public static final String TOPIC_VEHICLE_UPDATED = "vehicles.updated";
    public static final String TOPIC_VEHICLE_DELETED = "vehicles.deleted";

    @Bean
    public NewTopic vehicleValidationRequestTopic() {
        return TopicBuilder.name(TOPIC_VEHICLE_VALIDATION_REQUEST)
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic vehicleValidationResponseTopic() {
        return TopicBuilder.name(TOPIC_VEHICLE_VALIDATION_RESPONSE)
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic vehiclesCreatedTopic() {
        return TopicBuilder.name(TOPIC_VEHICLE_CREATED)
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic vehiclesUpdatedTopic() {
        return TopicBuilder.name(TOPIC_VEHICLE_UPDATED)
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic vehiclesDeletedTopic() {
        return TopicBuilder.name(TOPIC_VEHICLE_DELETED)
                .partitions(3)
                .replicas(1)
                .build();
    }
}
