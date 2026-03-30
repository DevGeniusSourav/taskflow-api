package com.sourav.taskflow.controller;

import com.sourav.taskflow.dto.ApiResponse;
import com.sourav.taskflow.dto.projects.CreateProjectRequest;
import com.sourav.taskflow.dto.projects.ProjectResponse;
import com.sourav.taskflow.dto.projects.UpdateProjectRequest;
import com.sourav.taskflow.service.ProjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/projects")
public class ProjectController {

    private final ProjectService projectService;

    @PostMapping
    public ResponseEntity<ApiResponse<ProjectResponse>> createProject(@Valid @RequestBody CreateProjectRequest projectRequest) {
        return ResponseEntity.ok(new ApiResponse<>(projectService.createProject(projectRequest), "Project Created Successfully"));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<ProjectResponse>>> getProjects(Pageable pageable) {
        return ResponseEntity.ok(new ApiResponse<>(projectService.getProjects(pageable), "Projects Fetched Successfully"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProjectResponse>> getProject(@PathVariable Long id) {
        return ResponseEntity.ok(new ApiResponse<>(projectService.getProject(id), "Project Fetched Successfully"));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<ProjectResponse>> updateProject(@PathVariable Long id, @Valid @RequestBody UpdateProjectRequest projectRequest) {
        return ResponseEntity.ok(new ApiResponse<>(projectService.updateProject(id, projectRequest), "Project Updated Successfully"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteProject(@PathVariable Long id) {
        projectService.deleteProject(id);
        return ResponseEntity.ok(new ApiResponse<>("Project Deleted!"));
    }

    @PutMapping("/restore/{id}")
    public ResponseEntity<ApiResponse<String>> restoreProject(@PathVariable Long id) {
        projectService.restoreProject(id);
        return ResponseEntity.ok(new ApiResponse<>("Project Restored!"));
    }
}
