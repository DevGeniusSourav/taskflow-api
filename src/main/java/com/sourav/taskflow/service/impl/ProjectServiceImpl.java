package com.sourav.taskflow.service.impl;

import com.sourav.taskflow.dto.projects.CreateProjectRequest;
import com.sourav.taskflow.dto.projects.ProjectResponse;
import com.sourav.taskflow.dto.projects.UpdateProjectRequest;
import com.sourav.taskflow.entity.Project;
import com.sourav.taskflow.entity.Task;
import com.sourav.taskflow.entity.User;
import com.sourav.taskflow.enums.Role;
import com.sourav.taskflow.exception.AccessDeniedException;
import com.sourav.taskflow.exception.GeneralException;
import com.sourav.taskflow.exception.ResourceNotFoundException;
import com.sourav.taskflow.repository.ProjectRepository;
import com.sourav.taskflow.repository.TaskRepository;
import com.sourav.taskflow.repository.UserRepository;
import com.sourav.taskflow.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final TaskRepository taskRepository;

    @Override
    public ProjectResponse createProject(CreateProjectRequest createProjectRequest) {

        User user = getCurrentUser();

        Project project = new Project();
        project.setName(createProjectRequest.getName());
        project.setDescription(createProjectRequest.getDescription());
        project.setOwner(user);

        return mapToResponse(projectRepository.save(project));
    }

    @Override
    public Page<ProjectResponse> getProjects(Pageable pageable) {

        User user = getCurrentUser();

        Page<Project> projects;

        if (user.getRole() == Role.ADMIN) {
            projects = projectRepository.findAll(pageable);
        } else {
            projects = projectRepository.findByOwnerId(user.getId(), pageable);
        }

        return projects.map(this::mapToResponse);
    }

    @Override
    public ProjectResponse getProject(Long id) {
        User user = getCurrentUser();

        Project project = projectRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Project Not Found!"));

        validateProjectOwnership(project, user);

        return mapToResponse(project);
    }

    @Override
    public ProjectResponse updateProject(Long id, UpdateProjectRequest updateProjectRequest) {

        User user = getCurrentUser();

        Project savedProject = projectRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Project Fot Found!"));

        validateProjectOwnership(savedProject, user);

        if (!updateProjectRequest.getName().isBlank()) {
            savedProject.setName(updateProjectRequest.getName());
        }

        if (!updateProjectRequest.getDescription().isBlank()) {
            savedProject.setDescription(updateProjectRequest.getDescription());
        }

        return mapToResponse(projectRepository.save(savedProject));
    }

    @Override
    public void deleteProject(Long id) {
        User user = getCurrentUser();

        Project project = projectRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Project Not Found!"));
        Optional<List<Task>> task  = taskRepository.findByProjectId(id);

        validateProjectOwnership(project, user);

        if(!task.isEmpty()) {
            throw new GeneralException("Cannot Delete Projects With Tasks!");
        }

        project.setDeleted(true);
        project.setDeletedBy(user.getId());
        project.setDeletedAt(LocalDateTime.now());

        projectRepository.save(project);
    }

    @Override
    public void restoreProject(Long id) {
        User user = getCurrentUser();

        Project project = projectRepository.findByIdIncludingDeleted(id).orElseThrow(() -> new ResourceNotFoundException("Project Not Found!"));

        if(!project.isDeleted()){
            throw new ResourceNotFoundException("Project Not Deleted!");
        }

        validateProjectOwnership(project, user);

        project.setDeleted(false);
        project.setDeletedBy(null);
        project.setDeletedAt(null);
    }


    private ProjectResponse mapToResponse(Project project) {
        return new ProjectResponse(
                project.getId(),
                project.getName(),
                project.getDescription(),
                project.getOwner().getId(),
                project.getOwner().getName(),
                project.getCreatedAt()
        );
    }

    public void validateProjectOwnership(Project project, User user) {
        if (!project.getOwner().getId().equals(user.getId()) && user.getRole() != Role.ADMIN) {
            throw new AccessDeniedException("Access Denied");
        }
    }

    private User getCurrentUser() {
        String email = Objects.requireNonNull(SecurityContextHolder
                        .getContext()
                        .getAuthentication())
                .getName();

        return userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }
}
