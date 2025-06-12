package com.altix.ezpark.reservations.application.internal.outboundservices.acl;

public interface ScheduleContextFacade {
    boolean isScheduleAvailable(Long scheduleId) throws Exception;
    void markScheduleAsUnavailable(Long scheduleId);
}
