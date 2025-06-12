package com.altix.ezpark.parkings.domain.model.commands;

public record DeleteScheduleCommand(Long scheduleId) {

    public DeleteScheduleCommand {
        if (scheduleId == null) {
            throw new IllegalArgumentException("Schedule ID cannot be null");
        }
    }
}
