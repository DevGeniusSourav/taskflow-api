package com.sourav.taskflow.service;

import com.sourav.taskflow.dto.CreateTaskRequest;
import com.sourav.taskflow.dto.TaskResponse;
import com.sourav.taskflow.entity.Task;

import java.util.List;

public interface TaskService {

    TaskResponse createTask(CreateTaskRequest task);

    List<Task> getTasksByProject(Long projectId);

    List<Task> getTasksByStatus(String status);

    List<Task> getTasksByProjectAndStatus(Long projectId, String status);
}
