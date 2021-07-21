package com.scheduler;

import com.scheduler.service.SchedulerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class Scheduler {
    private SchedulerService schedulerService;

    @Autowired
    public Scheduler(SchedulerService schedulerService) {
        this.schedulerService = schedulerService;
    }

    //every hour
    @Scheduled(cron = "0 0 * * * *")
    public void run() {
        schedulerService.retryToRunTasks();
    }
}
