package com.altix.ezpark.shared.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class JacksonConfig {
    @Bean
    @Primary
    public ObjectMapper objectMapper() {
        // 1. Instanciar el ObjectMapper.
        ObjectMapper mapper = new ObjectMapper();

        // 2. Registrar el módulo que añade soporte para los tipos de java.time.
        //    Esto es crucial para manejar Instant, LocalDateTime, ZonedDateTime, etc.
        mapper.registerModule(new JavaTimeModule());

        // 3. Deshabilitar la escritura de fechas como timestamps numéricos.
        //    Al deshabilitarlo, Jackson usará el formato de texto estándar ISO-8601,
        //    que es mucho más legible para los humanos y estándar para las APIs.
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        // Se podrían añadir otras configuraciones globales aquí si fueran necesarias, por ejemplo:
        // mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL); // No incluir campos nulos en el JSON
        // mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false); // Ignorar propiedades desconocidas al deserializar

        return mapper;
    }
}
