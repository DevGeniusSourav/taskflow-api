package com.sourav.taskflow.service.impl;

import com.sourav.taskflow.dto.CreateTaskRequest;
import com.sourav.taskflow.dto.TaskResponse;
import com.sourav.taskflow.dto.auth.UpdateTaskRequest;
import com.sourav.taskflow.entity.Project;
import com.sourav.taskflow.entity.Task;
import com.sourav.taskflow.entity.User;
import com.sourav.taskflow.enums.Role;
import com.sourav.taskflow.event.TaskCreatedEvent;
import com.sourav.taskflow.event.TaskDeletedEvent;
import com.sourav.taskflow.event.TaskUpdatedEvent;
import com.sourav.taskflow.exception.AccessDeniedException;
import com.sourav.taskflow.exception.ResourceNotFoundException;
import com.sourav.taskflow.repository.ProjectRepository;
import com.sourav.taskflow.repository.TaskRepository;
import com.sourav.taskflow.repository.UserRepository;
import com.sourav.taskflow.service.TaskService;
import com.sourav.taskflow.enums.TaskStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    public TaskResponse createTask(CreateTaskRequest taskRequest) {
        Project project = projectRepository.findById(taskRequest.getProjectId()).orElseThrow(() -> new ResourceNotFoundException("Project not found"));
        User user = getCurrentUser();

        Task task = new Task();
        task.setTitle(taskRequest.getTitle());
        task.setDescription(taskRequest.getDescription());
        task.setStatus(TaskStatus.TODO);
        task.setProject(project);
        task.setAssignee(user);
        task.setCreatedBy(user.getId());

        Task savedTask = taskRepository.save(task);

        eventPublisher.publishEvent(new TaskCreatedEvent(savedTask.getId(), savedTask.getAssignee().getId()));

        return mapToResponse(savedTask);

    }

    @Override
    public TaskResponse updateTask(Long id, UpdateTaskRequest taskRequest) {
        Task task = taskRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Task not found"));

        User user = getCurrentUser();

        if (!task.getAssignee().getId().equals(user.getId()) && user.getRole() != Role.ADMIN) {
            throw new AccessDeniedException("Access denied");
        }

        if (taskRequest.getTitle() != null) {
            task.setTitle(taskRequest.getTitle());
        }
        if (taskRequest.getDescription() != null) {
            task.setDescription(taskRequest.getDescription());
        }
        if (taskRequest.getStatus() != null) {
            task.setStatus(taskRequest.getStatus());
        }

        if (taskRequest.getAssigneeId() != null) {
            if (user.getRole() != Role.ADMIN) {
                throw new AccessDeniedException("Only ADMIN Can Update Assignee");
            }
            User newAssignee = userRepository.findById(taskRequest.getAssigneeId()).orElseThrow(() -> new ResourceNotFoundException("User not found"));
            task.setAssignee(newAssignee);
        }
        task.setUpdatedBy(user.getId());

        eventPublisher.publishEvent(new TaskUpdatedEvent(task.getId(), task.getAssignee().getId()));

        return mapToResponse(taskRepository.save(task));
    }

    @Override
    public void deleteTask(Long id) {
        Task task = taskRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Task not found"));

        User user = getCurrentUser();

        if (!task.getAssignee().getId().equals(user.getId()) && user.getRole() != Role.ADMIN) {
            throw new AccessDeniedException("Access denied");
        }
        task.setDeleted(true);
        task.setDeletedBy(user.getId());
        task.setDeletedAt(LocalDateTime.now());

        eventPublisher.publishEvent(new TaskDeletedEvent(task.getId(), task.getAssignee().getId()));

        taskRepository.delete(task);
    }

    @Override
    @Transactional(readOnly = true)
    public TaskResponse getTaskById(Long id) {
        Task task = taskRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Task not found"));

        User user = getCurrentUser();

        if (!task.getAssignee().getId().equals(user.getId()) && user.getRole() != Role.ADMIN) {
            throw new AccessDeniedException("Access denied");
        }
        return mapToResponse(task);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TaskResponse> getTasks(TaskStatus status, Long projectId, Pageable pageable) {
        Page<Task> tasks;

        User user = getCurrentUser();

        if (status != null && projectId != null) {
            tasks = taskRepository.findByProjectIdAndStatusAndAssigneeId(projectId, status, user.getId(), pageable);
        } else if (status != null) {
            tasks = taskRepository.findByStatusAndAssigneeId(status, user.getId(), pageable);
        } else if (projectId != null) {
            tasks = taskRepository.findByProjectIdAndAssigneeId(projectId, user.getId(), pageable);
        } else {
            if (user.getRole() == Role.ADMIN) {
                tasks = taskRepository.findAll(pageable);
            } else {
                tasks = taskRepository.findByAssigneeId(user.getId(), pageable);
            }
        }
        return tasks.map(this::mapToResponse);
    }

    @Override
    public void restoreTask(Long id) {
        Task task = taskRepository.findByIdIncludingDeleted(id).orElseThrow(() -> new ResourceNotFoundException("Task not found"));

        if (!task.isDeleted()) {
            throw new ResourceNotFoundException("Task is not deleted");
        }

        User user = getCurrentUser();

        if (!task.getAssignee().getId().equals(user.getId()) && user.getRole() != Role.ADMIN) {
            throw new AccessDeniedException("Access denied");
        }

        task.setDeleted(false);
        task.setDeletedAt(null);
        task.setDeletedBy(null);

        taskRepository.save(task);
    }

    private User getCurrentUser() {
        String email = Objects.requireNonNull(SecurityContextHolder
                        .getContext()
                        .getAuthentication())
                .getName();

        return userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    private TaskResponse mapToResponse(Task task) {
        return new TaskResponse(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getStatus(),
                task.getProject().getId(),
                task.getProject().getName(),
                task.getAssignee().getId(),
                task.getAssignee().getName(),
                task.getCreatedAt()
        );
    }
}
