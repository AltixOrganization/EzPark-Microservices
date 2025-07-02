package com.altix.ezpark.parkings.application.dtos;

public record ParkingQueryRequest(
  String correlationId, 
  Long entityId, 
  Long reservationId,
  Long scheduleId,   
  ParkingQueryType queryType
  ) {
    public ParkingQueryRequest(String correlationId, Long entityId, ParkingQueryType queryType) {
        this(correlationId, entityId, null, null, queryType);
    }
    
    public ParkingQueryRequest(String correlationId, Long entityId, Long scheduleId, ParkingQueryType queryType) {
        this(correlationId, entityId, null, scheduleId, queryType);
    }
}
