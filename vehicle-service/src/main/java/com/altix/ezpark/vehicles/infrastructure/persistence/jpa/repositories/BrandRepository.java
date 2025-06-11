package com.altix.ezpark.vehicles.infrastructure.persistence.jpa.repositories;


import com.altix.ezpark.vehicles.domain.model.entities.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BrandRepository extends JpaRepository<Brand, Long> {
}
