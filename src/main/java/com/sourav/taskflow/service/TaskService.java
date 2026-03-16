package com.sourav.taskflow.service;

import com.sourav.taskflow.dto.CreateTaskRequest;
import com.sourav.taskflow.dto.TaskResponse;
import com.sourav.taskflow.enums.TaskStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TaskService {

    TaskResponse createTask(CreateTaskRequest task);

    Page<TaskResponse> getTasks(TaskStatus status, Long projectId, Pageable pageable);
}
