package com.altix.ezpark.notifications.infrastructure.persistence.jpa.repositories;

import com.altix.ezpark.notifications.domain.model.aggregates.EmailLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailLogRepository extends JpaRepository<EmailLog, Long> {
}
