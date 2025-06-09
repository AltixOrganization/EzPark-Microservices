package com.altix.ezpark.iam.infrastructure.messaging;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class IamKafkaConfig {
    public static final String TOPIC_USER_SIGNED_UP = "iam.events.user.signed-up";

    @Bean
    public NewTopic userSignedUpTopic() {
        return TopicBuilder.name(TOPIC_USER_SIGNED_UP)
                .partitions(3)
                .replicas(1)
                .build();
    }
}
