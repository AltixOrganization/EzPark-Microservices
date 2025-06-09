package com.altix.ezpark.shared.infrastructure.messaging;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ResponseRegistry {
    private final ConcurrentHashMap<String, CompletableFuture<Object>> pendingRequests = new ConcurrentHashMap<>();
    private final long timeoutMillis = 5000;

    public <T> T waitForResponse(String correlationId, Class<T> type) throws Exception {
        CompletableFuture<Object> future = new CompletableFuture<>();
        pendingRequests.put(correlationId, future);

        try {
            Object result = future.get(timeoutMillis, TimeUnit.MILLISECONDS);
            return type.cast(result);
        } finally {
            pendingRequests.remove(correlationId);
        }
    }

    public void complete(String correlationId, Object response) {
        CompletableFuture<Object> future = pendingRequests.get(correlationId);
        if (future != null) {
            future.complete(response);
        }
    }
}
