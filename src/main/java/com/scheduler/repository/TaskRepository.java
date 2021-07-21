package com.scheduler.repository;

import com.scheduler.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public interface TaskRepository extends JpaRepository<Task, UUID> {
    List<Task> findByDoneIsFalseAndExecuteTimeAfter(Date date);

    List<Task> findByDoneIsFalseAndExecuteTimeBefore(Date date);
}
