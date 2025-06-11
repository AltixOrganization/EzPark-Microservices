package com.altix.ezpark.profiles.infrastructure.persistence.jpa.repositories;


import com.altix.ezpark.profiles.domain.model.aggregates.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long> {
    boolean existsByUserId_UserId(Long userId);
}
