package com.altix.ezpark.notifications.interfaces.rest;

import com.altix.ezpark.notifications.domain.model.queries.GetAllEmailLogsQuery;
import com.altix.ezpark.notifications.domain.service.EmailLogQueryService;
import com.altix.ezpark.notifications.interfaces.rest.resources.EmailLogResource;
import com.altix.ezpark.notifications.interfaces.rest.transform.EmailLogResourceFromEntityAssembler;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/email-log")
@Tag(name = "EmailLog", description = "User Management Endpoints")
public class EmailLogController {
    private final EmailLogQueryService emailLogQueryService;

    public EmailLogController(EmailLogQueryService emailLogQueryService) {
        this.emailLogQueryService = emailLogQueryService;
    }

    @GetMapping
    public ResponseEntity<List<EmailLogResource>> getAllEmailLogs() {
        var emailLogs = emailLogQueryService.handle(new GetAllEmailLogsQuery());
        var emailLogResources = emailLogs.stream()
                .map(EmailLogResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return ResponseEntity.ok(emailLogResources);
    }
}
