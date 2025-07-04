package com.altix.ezpark.notifications.domain.service;

import com.altix.ezpark.notifications.domain.model.aggregates.EmailLog;
import com.altix.ezpark.notifications.domain.model.queries.GetAllEmailLogsQuery;

import java.util.List;

public interface EmailLogQueryService {
    List<EmailLog> handle(GetAllEmailLogsQuery query);
}
