package com.altix.ezpark.vehicles.application.internal.commandservices;

import java.util.Optional;

import com.altix.ezpark.vehicles.domain.model.commands.CreateModelCommand;
import com.altix.ezpark.vehicles.domain.model.commands.DeleteModelCommand;
import com.altix.ezpark.vehicles.domain.model.commands.UpdateModelCommand;
import com.altix.ezpark.vehicles.domain.model.entities.Model;
import com.altix.ezpark.vehicles.domain.model.exceptions.BrandNotFoundException;
import com.altix.ezpark.vehicles.domain.model.exceptions.ModelNotFoundException;
import com.altix.ezpark.vehicles.domain.services.ModelCommandService;
import com.altix.ezpark.vehicles.infrastructure.persistence.jpa.repositories.BrandRepository;
import com.altix.ezpark.vehicles.infrastructure.persistence.jpa.repositories.ModelRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class ModelCommandServiceImpl implements ModelCommandService {
    private ModelRepository modelRepository;
    private BrandRepository brandRepository;

    @Override
    public Optional<Model> handle(CreateModelCommand command) {
        var brand = brandRepository.findById(command.brandId());
        if (brand.isEmpty()) {
      throw new BrandNotFoundException();
        }
        var model = new Model(command);
        model.setBrand(brand.get());
        modelRepository.save(model);

        return Optional.of(model);
    }

    @Override
    public Optional<Model> handle(UpdateModelCommand command) {
        var model = modelRepository.findById(command.modelId());
        if (model.isEmpty()) {
            throw new ModelNotFoundException();
        }
        var brand = brandRepository.findById(command.brandId());
        if (brand.isEmpty()) {
            throw new BrandNotFoundException();
        }
        model.get().update(command, brand.get());
        modelRepository.save(model.get());
        return model;
    }

    @Override
    public void handle(DeleteModelCommand command) {
        if (!modelRepository.existsById(command.id())) {
            throw new ModelNotFoundException();
        }
        modelRepository.deleteById(command.id());
    }
}
