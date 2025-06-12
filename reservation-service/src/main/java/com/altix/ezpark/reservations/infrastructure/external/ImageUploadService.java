package com.altix.ezpark.reservations.infrastructure.external;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class ImageUploadService {
    private final WebClient webClient;
    private final String apiKey;

    public ImageUploadService(WebClient.Builder webClientBuilder,
                              @Value("${imgbb.api.url}") String apiUrl,
                              @Value("${imgbb.api.key}") String apiKey) {
        this.webClient = webClientBuilder.baseUrl(apiUrl).build();
        this.apiKey = apiKey;
    }

    public Mono<ImgbbResponse> uploadImage(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return Mono.error(new IllegalArgumentException("El archivo de imagen no puede estar vacío"));
        }

        MultipartBodyBuilder builder = new MultipartBodyBuilder();

        builder.part("key", apiKey);

        Resource imageResource = file.getResource();
        builder.part("image", imageResource);

        MultiValueMap<String, HttpEntity<?>> multipartBody = builder.build();

        return webClient.post()
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData(multipartBody))
                .retrieve()
                .onStatus(status -> status.is4xxClientError() || status.is5xxServerError(),
                        clientResponse -> clientResponse.bodyToMono(String.class)
                                .flatMap(errorBody -> Mono.error(new RuntimeException("API responded with status " + clientResponse.statusCode() + " and body: " + errorBody))))
                .bodyToMono(ImgbbResponse.class);
    }

}
