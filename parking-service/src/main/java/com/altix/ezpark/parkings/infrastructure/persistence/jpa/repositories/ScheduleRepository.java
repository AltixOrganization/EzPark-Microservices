package com.altix.ezpark.parkings.infrastructure.persistence.jpa.repositories;

import com.altix.ezpark.parkings.domain.model.entities.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    Optional<Schedule> findByDayAndStartTimeAndEndTime(LocalDate day, LocalTime startTime, LocalTime endTime);
}
