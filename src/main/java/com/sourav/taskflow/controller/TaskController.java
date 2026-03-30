package com.sourav.taskflow.controller;

import com.sourav.taskflow.dto.ApiResponse;
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
    public ApiResponse<TaskResponse> createTask(@Valid @RequestBody CreateTaskRequest taskRequest) {
        return new ApiResponse<>(taskService.createTask(taskRequest), "Task Created Successfully");
    }

    @GetMapping
    @Operation(summary = "Get tasks with pagination and filters")
    public ApiResponse<Page<TaskResponse>> getTasks(
            @RequestParam(required = false) TaskStatus status,
            @RequestParam(required = false) Long projectId,
            Pageable pageable) {
        return new ApiResponse<>(taskService.getTasks(status, projectId, pageable), "Tasks Fetched Successfully");
    }

    @GetMapping("/{id}")
    public ApiResponse<TaskResponse> getTask(@PathVariable Long id) {
        return new ApiResponse<>(taskService.getTaskById(id), "Task Fetched Successfully");
    }

    @PatchMapping("/{id}")
    public ApiResponse<TaskResponse> updateTask(@PathVariable Long id, @RequestBody UpdateTaskRequest taskRequest) {
        return new ApiResponse<>(taskService.updateTask(id, taskRequest), "Task Updated Successfully");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.ok(new ApiResponse<>("Task Deleted Successfully"));
    }

    @PutMapping("/restore/{id}")
    public ResponseEntity<ApiResponse<String>> restoreTask(@PathVariable Long id) {
        taskService.restoreTask(id);
        return ResponseEntity.ok(new ApiResponse<>("Task Restored Successfully"));
    }

}
