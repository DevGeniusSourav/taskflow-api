package com.sourav.taskflow.service;

import com.sourav.taskflow.dto.tasks.CreateTaskRequest;
import com.sourav.taskflow.dto.tasks.TaskResponse;
import com.sourav.taskflow.dto.tasks.UpdateTaskRequest;
import com.sourav.taskflow.enums.TaskStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TaskService {

    TaskResponse createTask(CreateTaskRequest task);

    TaskResponse updateTask(Long id, UpdateTaskRequest task);

    void deleteTask(Long id);

    Page<TaskResponse> getTasks(TaskStatus status, Long projectId, Pageable pageable);

    public void restoreTask(Long id);
}
