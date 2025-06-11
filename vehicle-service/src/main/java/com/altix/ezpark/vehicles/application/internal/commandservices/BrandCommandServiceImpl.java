package com.altix.ezpark.vehicles.application.internal.commandservices;


import java.util.Optional;

import com.altix.ezpark.vehicles.domain.model.commands.CreateBrandCommand;
import com.altix.ezpark.vehicles.domain.model.commands.DeleteBrandCommand;
import com.altix.ezpark.vehicles.domain.model.commands.UpdateBrandCommand;
import com.altix.ezpark.vehicles.domain.model.entities.Brand;
import com.altix.ezpark.vehicles.domain.model.exceptions.BrandNotFoundException;
import com.altix.ezpark.vehicles.domain.services.BrandCommandService;
import com.altix.ezpark.vehicles.infrastructure.persistence.jpa.repositories.BrandRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class BrandCommandServiceImpl implements BrandCommandService {
    private BrandRepository brandRepository;

    @Override
    public Optional<Brand> handle(CreateBrandCommand command) {
        var brand = new Brand(command);
        brandRepository.save(brand);
        return Optional.of(brand);
    }

    @Override
    public Optional<Brand> handle(UpdateBrandCommand command) {
        var brand = brandRepository.findById(command.brandId());
        if (brand.isEmpty()) {
            throw new BrandNotFoundException();
        }
        brand.get().update(command);
        brandRepository.save(brand.get());
        return brand;
    }

    @Override
    public void handle(DeleteBrandCommand command) {
        if (!brandRepository.existsById(command.id())){
            throw new BrandNotFoundException();
        }
        brandRepository.deleteById(command.id());
    }
}
