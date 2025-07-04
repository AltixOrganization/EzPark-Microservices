package com.altix.ezpark.notifications.domain.service;

import com.altix.ezpark.notifications.domain.model.commands.CreateEmailLogCommand;

public interface EmailLogCommandService {
    Long handle(CreateEmailLogCommand command);
}
