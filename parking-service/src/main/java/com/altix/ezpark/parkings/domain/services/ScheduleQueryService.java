package com.altix.ezpark.parkings.domain.services;

import com.altix.ezpark.parkings.domain.model.entities.Schedule;
import com.altix.ezpark.parkings.domain.model.queries.GetAllScheduleQuery;
import com.altix.ezpark.parkings.domain.model.queries.GetScheduleByDayStartTimeAndEndTimeQuery;
import com.altix.ezpark.parkings.domain.model.queries.GetScheduleByIdQuery;

import java.util.List;
import java.util.Optional;

public interface ScheduleQueryService {
    List<Schedule> handle(GetAllScheduleQuery query);
    Optional<Schedule> handle(GetScheduleByDayStartTimeAndEndTimeQuery query);
    Optional<Schedule> handle(GetScheduleByIdQuery query);
}
