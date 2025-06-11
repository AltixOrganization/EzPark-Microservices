package com.altix.ezpark.vehicles.infrastructure.persistence.jpa.repositories;


import com.altix.ezpark.vehicles.domain.model.entities.Model;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ModelRepository extends JpaRepository<Model, Long> {
}
