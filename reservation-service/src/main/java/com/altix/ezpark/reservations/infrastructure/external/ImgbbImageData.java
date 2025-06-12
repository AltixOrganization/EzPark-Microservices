package com.altix.ezpark.reservations.infrastructure.external;

public record ImgbbImageData(
        String filename,
        String name,
        String mime,
        String extension,
        String url
) {
}
