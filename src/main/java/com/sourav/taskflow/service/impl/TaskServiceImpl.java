package com.sourav.taskflow.service.impl;

import com.sourav.taskflow.entity.Task;
import com.sourav.taskflow.repository.TaskRepository;
import com.sourav.taskflow.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;

    @Override
    public Task createTask(Task task) {
        return taskRepository.save(task);
    }

    @Override
    public List<Task> getTasksByProject(Long projectId) {
        return taskRepository.findByProjectId(projectId);
    }

    @Override
    public List<Task> getTasksByStatus(String status) {
        return taskRepository.findByStatus(status);
    }

    @Override
    public List<Task> getTasksByProjectAndStatus(Long projectId, String status) {
        return taskRepository.findByProjectIdAndStatus(projectId, status);
    }
}
