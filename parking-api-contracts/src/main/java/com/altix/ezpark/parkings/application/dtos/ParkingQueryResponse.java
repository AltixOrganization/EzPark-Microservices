package com.altix.ezpark.parkings.application.dtos;

public record ParkingQueryResponse(String correlationId, boolean result, String message) {
}
