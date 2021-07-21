package com.scheduler.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ScheduledFuture;

@Service
public class ScheduleTaskService {
    private TaskScheduler scheduler;

    // A map for keeping scheduled tasks
    private Map<UUID, ScheduledFuture<?>> jobsMap = new HashMap<>();

    @Autowired
    public ScheduleTaskService(@Qualifier("threadPoolTaskScheduler") TaskScheduler scheduler) {
        this.scheduler = scheduler;
    }

    /**
     * Schedule Task to be executed on given date
     */
    public void addTaskToScheduler(UUID id, Runnable task, Date executeTime) {
        ScheduledFuture<?> scheduledTask = scheduler.schedule(task, executeTime);
        jobsMap.put(id, scheduledTask);
    }

    /**
     * Remove scheduled task
     */
    public void removeTaskFromScheduler(UUID id) {
        ScheduledFuture<?> scheduledTask = jobsMap.get(id);
        if (scheduledTask != null) {
            scheduledTask.cancel(true);
            jobsMap.remove(id);
        }
    }
}
