package com.sourav.taskflow.controller;

import com.sourav.taskflow.dto.CreateTaskRequest;
import com.sourav.taskflow.dto.TaskResponse;
import com.sourav.taskflow.entity.Task;
import com.sourav.taskflow.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @PostMapping
    public TaskResponse createTask(@Valid @RequestBody CreateTaskRequest taskRequest) {
        return taskService.createTask(taskRequest);
    }

    @GetMapping("/project/{projectId}")
    public List<Task> getTaskByProjectId(@PathVariable Long projectId) {
        return taskService.getTasksByProject(projectId);
    }

    @GetMapping("/status/{status}")
    public List<Task> getTaskByStatus(@PathVariable String status) {
        return taskService.getTasksByStatus(status);
    }

    @GetMapping("/project/{projectId}/status/{statusId}")
    public List<Task> getTaskByProjectAndStatus(@PathVariable Long projectId, @PathVariable String statusId) {
        return taskService.getTasksByProjectAndStatus(projectId, statusId);
    }

}
