package com.altix.ezpark.parkings.application.dtos;

public record ParkingCommandRequest(String correlationId, Long scheduleId, ParkingCommandType commandType) {
}
