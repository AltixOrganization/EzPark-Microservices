package com.altix.ezpark.notifications.application.internal.queryservice;

import com.altix.ezpark.notifications.domain.model.aggregates.EmailLog;
import com.altix.ezpark.notifications.domain.model.queries.GetAllEmailLogsQuery;
import com.altix.ezpark.notifications.domain.service.EmailLogQueryService;
import com.altix.ezpark.notifications.infrastructure.persistence.jpa.repositories.EmailLogRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class EmailLogQueryServiceImpl implements EmailLogQueryService {
    private final EmailLogRepository emailLogRepository;

    @Override
    public List<EmailLog> handle(GetAllEmailLogsQuery query) {
        return emailLogRepository.findAll();
    }
}
