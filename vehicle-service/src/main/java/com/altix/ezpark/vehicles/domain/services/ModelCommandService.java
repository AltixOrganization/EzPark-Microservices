package com.altix.ezpark.vehicles.domain.services;



import com.altix.ezpark.vehicles.domain.model.commands.CreateModelCommand;
import com.altix.ezpark.vehicles.domain.model.commands.DeleteModelCommand;
import com.altix.ezpark.vehicles.domain.model.commands.UpdateModelCommand;
import com.altix.ezpark.vehicles.domain.model.entities.Model;

import java.util.Optional;

public interface ModelCommandService {
    Optional<Model> handle(CreateModelCommand command);
    Optional<Model> handle(UpdateModelCommand command);
    void handle(DeleteModelCommand command);
}
