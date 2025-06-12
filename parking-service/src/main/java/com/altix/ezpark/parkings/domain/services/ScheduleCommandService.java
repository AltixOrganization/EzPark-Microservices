package com.altix.ezpark.parkings.domain.services;

import com.altix.ezpark.parkings.domain.model.commands.CreateScheduleCommand;
import com.altix.ezpark.parkings.domain.model.commands.DeleteScheduleCommand;
import com.altix.ezpark.parkings.domain.model.commands.MarkScheduleAsUnavailable;
import com.altix.ezpark.parkings.domain.model.commands.UpdateScheduleCommand;
import com.altix.ezpark.parkings.domain.model.entities.Schedule;

import java.util.Optional;

public interface ScheduleCommandService {
    Optional<Schedule> handle(CreateScheduleCommand command);
    Optional<Schedule> handle(UpdateScheduleCommand command);
    Optional<Schedule> handle(MarkScheduleAsUnavailable command);
    void handle(DeleteScheduleCommand command);
}
