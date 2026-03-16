package com.sourav.taskflow.service.impl;

import com.sourav.taskflow.dto.CreateTaskRequest;
import com.sourav.taskflow.dto.TaskResponse;
import com.sourav.taskflow.entity.Project;
import com.sourav.taskflow.entity.Task;
import com.sourav.taskflow.entity.User;
import com.sourav.taskflow.exception.ResourceNotFoundException;
import com.sourav.taskflow.repository.ProjectRepository;
import com.sourav.taskflow.repository.TaskRepository;
import com.sourav.taskflow.repository.UserRepository;
import com.sourav.taskflow.service.TaskService;
import com.sourav.taskflow.enums.TaskStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    @Override
    public TaskResponse createTask(CreateTaskRequest taskRequest) {
        Project project = projectRepository.findById(taskRequest.getProjectId()).orElseThrow(() -> new ResourceNotFoundException("Project not found"));
        User user = userRepository.findById(taskRequest.getAssigneeId()).orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Task task = new Task();
        task.setTitle(taskRequest.getTitle());
        task.setDescription(taskRequest.getDescription());
        task.setStatus(TaskStatus.TODO);
        task.setProject(project);
        task.setAssignee(user);

        Task savedTask = taskRepository.save(task);

        return mapToResponse(savedTask);

    }

    @Override
    @Transactional(readOnly = true)
    public Page<TaskResponse> getTasks(TaskStatus status, Long projectId, Pageable pageable) {
        Page<Task> tasks;

        if (status != null && projectId != null) {
            tasks = taskRepository.findByProjectIdAndStatus(projectId, status, pageable);
        } else if (status != null) {
            tasks = taskRepository.findByStatus(status, pageable);
        } else if (projectId != null) {
            tasks = taskRepository.findByProjectId(projectId, pageable);
        } else {
            tasks = taskRepository.findAll(pageable);
        }
        return tasks.map(this::mapToResponse);
    }

    private TaskResponse mapToResponse(Task task) {
        return new TaskResponse(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getStatus(),
                task.getProject().getId(),
                task.getAssignee().getId(),
                task.getCreatedAt()
        );
    }
}
