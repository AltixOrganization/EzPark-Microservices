package com.altix.ezpark.vehicles.application.internal.queryservices;

import java.util.List;

import com.altix.ezpark.vehicles.domain.model.entities.Brand;
import com.altix.ezpark.vehicles.domain.model.queries.GetAllBrandsQuery;
import com.altix.ezpark.vehicles.domain.services.BrandQueryService;
import com.altix.ezpark.vehicles.infrastructure.persistence.jpa.repositories.BrandRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class BrandQueryServiceImpl implements BrandQueryService {
    private BrandRepository brandRepository;

    @Override
    public List<Brand> handle(GetAllBrandsQuery query) {
        return brandRepository.findAll();
    }
}
