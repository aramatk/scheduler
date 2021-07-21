package com.scheduler.api;

import com.scheduler.dto.TaskRequest;
import com.scheduler.service.SchedulerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/scheduler")
public class SchedulerController {
    private SchedulerService schedulerService;

    @Autowired
    public SchedulerController(SchedulerService schedulerService) {
        this.schedulerService = schedulerService;
    }

    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<UUID> add(@RequestBody TaskRequest taskRequest) {
        UUID taskId = schedulerService.addTask(taskRequest);
        return new ResponseEntity<>(taskId, HttpStatus.CREATED);
    }

    @DeleteMapping("/delete/{taskId}")
    public void remove(@PathVariable UUID taskId) {
        schedulerService.removeTask(taskId);
    }
}
