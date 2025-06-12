package com.altix.ezpark.parkings.domain.model.queries;

import java.time.LocalDate;
import java.time.LocalTime;

public record GetScheduleByDayStartTimeAndEndTimeQuery(LocalDate day,
                                                      LocalTime startTime,
                                                       LocalTime endTime) {
}
