package com.altix.ezpark.parkings.application.dtos;

public record ParkingQueryRequest(String correlationId, Long entityId, ParkingQueryType queryType) {
}
