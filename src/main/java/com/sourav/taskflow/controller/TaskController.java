package com.sourav.taskflow.controller;

import com.sourav.taskflow.dto.CreateTaskRequest;
import com.sourav.taskflow.dto.TaskResponse;
import com.sourav.taskflow.service.TaskService;
import com.sourav.taskflow.enums.TaskStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
@Tag(name = "Task API", description = "Operations related to tasks")
public class TaskController {

    private final TaskService taskService;

    @PostMapping
    @Operation(summary = "Create a new task")
    public TaskResponse createTask(@Valid @RequestBody CreateTaskRequest taskRequest) {
        return taskService.createTask(taskRequest);
    }

    @GetMapping
    @Operation(summary = "Get tasks with pagination and filters")
    public Page<TaskResponse> getTasks(
            @RequestParam(required = false) TaskStatus status,
            @RequestParam(required = false) Long projectId,
            Pageable pageable) {
        return taskService.getTasks(status, projectId, pageable);
    }

}
