package com.sourav.taskflow.service;

import com.sourav.taskflow.entity.Task;

import java.util.List;

public interface TaskService {

    Task createTask(Task task);

    List<Task> getTasksByProject(Long projectId);

    List<Task> getTasksByStatus(String status);

    List<Task> getTasksByProjectAndStatus(Long projectId, String status);
}
