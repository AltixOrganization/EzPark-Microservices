package com.altix.ezpark.parkings.application.dtos;

public record ParkingCommandResponse(String correlationId, boolean success, String message) {
}
