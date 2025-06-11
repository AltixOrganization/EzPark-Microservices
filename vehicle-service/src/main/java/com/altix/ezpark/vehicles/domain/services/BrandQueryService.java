package com.altix.ezpark.vehicles.domain.services;


import com.altix.ezpark.vehicles.domain.model.entities.Brand;
import com.altix.ezpark.vehicles.domain.model.queries.GetAllBrandsQuery;

import java.util.List;

public interface BrandQueryService {
    List<Brand> handle(GetAllBrandsQuery query);
}
