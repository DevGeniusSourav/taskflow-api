package com.sourav.taskflow.service;

import com.sourav.taskflow.dto.projects.CreateProjectRequest;
import com.sourav.taskflow.dto.projects.ProjectResponse;
import com.sourav.taskflow.dto.projects.UpdateProjectRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProjectService {

    ProjectResponse createProject(CreateProjectRequest createProjectRequest);

    Page<ProjectResponse> getProjects(Pageable pageable);

    ProjectResponse getProject(Long id);

    ProjectResponse updateProject(Long id, UpdateProjectRequest updateProjectRequest);

    void deleteProject(Long id);

    void restoreProject(Long id);

}
