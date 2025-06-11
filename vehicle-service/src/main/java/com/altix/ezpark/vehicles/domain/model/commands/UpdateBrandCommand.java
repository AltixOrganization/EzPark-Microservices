package com.altix.ezpark.vehicles.domain.model.commands;

public record UpdateBrandCommand(Long brandId,String name, String description) {
}
