package com.scheduler.service;

import com.scheduler.model.Task;
import com.scheduler.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class TaskService {
    private TaskRepository taskRepository;

    @Autowired
    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public Task save(Task task) {
        return taskRepository.save(task);
    }

    public void delete(UUID id) {
        taskRepository.deleteById(id);
    }

    public List<Task> getFutureNotExecutedTasks() {
        return taskRepository.findByDoneIsFalseAndExecuteTimeAfter(new Date());
    }

    public List<Task> getPastNotExecutedTasks() {
        return taskRepository.findByDoneIsFalseAndExecuteTimeBefore(new Date());
    }
}
