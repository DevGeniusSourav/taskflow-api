package com.sourav.taskflow.controller;

import com.sourav.taskflow.dto.tasks.CreateTaskRequest;
import com.sourav.taskflow.dto.tasks.TaskResponse;
import com.sourav.taskflow.dto.tasks.UpdateTaskRequest;
import com.sourav.taskflow.service.TaskService;
import com.sourav.taskflow.enums.TaskStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
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

    @GetMapping("/{id}")
    public TaskResponse getTask(@PathVariable Long id) {
        return taskService.getTaskById(id);
    }

    @PatchMapping("/{id}")
    public TaskResponse updateTask(@PathVariable Long id, @RequestBody UpdateTaskRequest taskRequest) {
        return taskService.updateTask(id, taskRequest);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.ok("Task Deleted!");
    }

    @PutMapping("/restore/{id}")
    public ResponseEntity<?> restoreTask(@PathVariable Long id) {
        taskService.restoreTask(id);
        return ResponseEntity.ok("Task Restored!");
    }

}
