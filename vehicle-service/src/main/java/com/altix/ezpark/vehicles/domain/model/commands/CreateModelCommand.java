package com.altix.ezpark.vehicles.domain.model.commands;

public record CreateModelCommand(String name, String description, Long brandId) {
}
