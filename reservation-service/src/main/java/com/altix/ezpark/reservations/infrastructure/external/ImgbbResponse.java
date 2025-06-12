package com.altix.ezpark.reservations.infrastructure.external;

/**
 * @author amner
 * @date 4/05/2025
 */
public record ImgbbResponse(
        ImgbbData data,
        boolean success,
        int status
) {
}
