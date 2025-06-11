package com.altix.ezpark.vehicles.domain.services;


import com.altix.ezpark.vehicles.domain.model.commands.CreateBrandCommand;
import com.altix.ezpark.vehicles.domain.model.commands.DeleteBrandCommand;
import com.altix.ezpark.vehicles.domain.model.commands.UpdateBrandCommand;
import com.altix.ezpark.vehicles.domain.model.entities.Brand;

import java.util.Optional;

public interface BrandCommandService {
    Optional<Brand> handle(CreateBrandCommand command);
    Optional<Brand> handle(UpdateBrandCommand command);
    void handle(DeleteBrandCommand command);
}
