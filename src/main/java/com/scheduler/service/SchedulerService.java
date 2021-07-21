package com.scheduler.service;

import com.scheduler.dto.TaskRequest;
import com.scheduler.exceptions.SchedulerServiceException;
import com.scheduler.model.Task;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
@Slf4j
public class SchedulerService {
    private HttpService httpService;
    private ScheduleTaskService scheduleTaskService;
    private TaskService taskService;

    @Autowired
    public SchedulerService(HttpService httpService, ScheduleTaskService scheduleTaskService, TaskService taskService) {
        this.httpService = httpService;
        this.scheduleTaskService = scheduleTaskService;
        this.taskService = taskService;
    }

    public UUID addTask(TaskRequest taskRequest) {
        log.info("Adding new task to scheduler {}", taskRequest);
        UUID taskId = null;
        try {
            Task task = taskRequest.toTask();
            Task savedTask = taskService.save(task);
            taskId = savedTask.getId();
            addTaskToScheduler(savedTask);
        } catch (Exception e) {
            log.error("Exception happened on new task adding {}", e.getMessage());
            throw new SchedulerServiceException("Exception happened on new task adding", e);
        }
        log.info("Task is added with id {}", taskId);
        return taskId;
    }

    private void addTaskToScheduler(Task task) {
        scheduleTaskService.addTaskToScheduler(task.getId(), () -> taskRun(task), task.getExecuteTime());
    }

    private void taskRun(Task task) {
        log.info("Task is started {}", task.getId());
        try {
            httpService.run(task);
            task.setDone(true);
            taskService.save(task);
            scheduleTaskService.removeTaskFromScheduler(task.getId());
            log.info("Task is successfully done {}", task.getId());
        } catch (Exception e) {
            log.error("Exception happened on task {} running: {}", task.getId(), e.getMessage());
            throw new SchedulerServiceException("Error happened on task running " + task.getId(), e);
        }
    }

    public void removeTask(UUID taskId) {
        log.info("Removing task with id {}", taskId);
        try {
            taskService.delete(taskId);
            scheduleTaskService.removeTaskFromScheduler(taskId);
            log.info("Task with id {} is removed", taskId);
        } catch (Exception e) {
            log.error("Exception happened on task {} removing: {}", taskId, e.getMessage());
            throw new SchedulerServiceException("Error happened on task removing " + taskId, e);
        }
    }

    // A context refresh event listener
    @EventListener({ContextRefreshedEvent.class})
    void contextRefreshedEvent() {
        try {
            addFutureTaskToScheduler();
            retryToRunTasks();
        } catch (Exception e) {
            log.error("Exception happened on tasks initializing");
        }
    }

    private void addFutureTaskToScheduler() {
        // Get all future tasks from DB and reschedule them in case of context restarted
        List<Task> futureTaskList = taskService.getFutureNotExecutedTasks();
        log.info("Adding {} tasks to scheduler {}", futureTaskList.size(), futureTaskList);
        futureTaskList.stream().forEach(this::addTaskToScheduler);
    }

    /**
     * Gets past tasks that were not done and try to execute them again
     */
    public void retryToRunTasks() {
        // Get all past tasks from DB that were not executed and run them
        List<Task> pastNotExecutedTaskList = taskService.getPastNotExecutedTasks();
        log.info("Running {} tasks {}", pastNotExecutedTaskList.size(), pastNotExecutedTaskList);
        pastNotExecutedTaskList.stream().forEach(this::taskRun);
    }
}
