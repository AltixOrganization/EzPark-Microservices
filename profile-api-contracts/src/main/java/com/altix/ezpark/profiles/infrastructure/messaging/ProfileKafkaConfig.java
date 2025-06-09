package com.altix.ezpark.profiles.infrastructure.messaging;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class ProfileKafkaConfig {
    public static final String TOPIC_PROFILE_VALIDATION_REQUEST = "profiles.validation.request";
    public static final String TOPIC_PROFILE_VALIDATION_RESPONSE = "profiles.validation.response";

    public static final String TOPIC_PROFILE_CREATED = "profiles.created";
    public static final String TOPIC_PROFILE_UPDATED = "profiles.updated";
    public static final String TOPIC_PROFILE_DELETED = "profiles.deleted";

    @Bean
    public NewTopic profileValidationRequestTopic() {
        return TopicBuilder.name(TOPIC_PROFILE_VALIDATION_REQUEST)
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic profileValidationResponseTopic() {
        return TopicBuilder.name(TOPIC_PROFILE_VALIDATION_RESPONSE)
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic profilesCreatedTopic() {
        return TopicBuilder.name(TOPIC_PROFILE_CREATED)
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic profilesUpdatedTopic() {
        return TopicBuilder.name(TOPIC_PROFILE_UPDATED)
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic profilesDeletedTopic() {
        return TopicBuilder.name(TOPIC_PROFILE_DELETED)
                .partitions(3)
                .replicas(1)
                .build();
    }
}
