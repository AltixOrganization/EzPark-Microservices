package com.altix.ezpark.notifications.application.internal.commandservice;

import com.altix.ezpark.notifications.domain.model.aggregates.EmailLog;
import com.altix.ezpark.notifications.domain.model.commands.CreateEmailLogCommand;
import com.altix.ezpark.notifications.domain.service.EmailLogCommandService;
import com.altix.ezpark.notifications.infrastructure.persistence.jpa.repositories.EmailLogRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class EmailLogCommandServiceImpl implements EmailLogCommandService {
    private final EmailLogRepository emailLogRepository;

    @Override
    public Long handle(CreateEmailLogCommand command) {
        var emailLog = new EmailLog(command);
        emailLogRepository.save(emailLog);
        return emailLog.getId();
    }
}
