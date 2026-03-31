package com.sourav.taskflow.service;

import com.sourav.taskflow.dto.tasks.CreateTaskRequest;
import com.sourav.taskflow.dto.tasks.TaskResponse;
import com.sourav.taskflow.dto.tasks.UpdateTaskRequest;
import com.sourav.taskflow.entity.Project;
import com.sourav.taskflow.entity.Task;
import com.sourav.taskflow.entity.User;
import com.sourav.taskflow.enums.Role;
import com.sourav.taskflow.enums.TaskStatus;
import com.sourav.taskflow.event.tasks.TaskCreatedEvent;
import com.sourav.taskflow.repository.ProjectRepository;
import com.sourav.taskflow.repository.TaskRepository;
import com.sourav.taskflow.repository.UserRepository;
import com.sourav.taskflow.service.impl.TaskServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TaskServiceImplTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ApplicationEventPublisher applicationEventPublisher;

    @InjectMocks
    private TaskServiceImpl taskService;

    String email = "user@test.com";

    @Test
    void createTask_shouldCreateTaskSuccessfully() {
        mockCurrentUser(email);

        User currentUser = new User();
        currentUser.setId(1L);

        Project project = new Project();
        project.setId(1L);
        project.setName("test");

        User assignee = new User();
        assignee.setId(2L);
        assignee.setName("assignee");

        CreateTaskRequest createTaskRequest = new CreateTaskRequest();
        createTaskRequest.setTitle("Task Title");
        createTaskRequest.setDescription("Task Description");
        createTaskRequest.setProjectId(10L);
        createTaskRequest.setAssigneeId(2L);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(currentUser));
        when(projectRepository.findById(10L)).thenReturn(Optional.of(project));
        when(userRepository.findById(2L)).thenReturn(Optional.of(assignee));

        Task savedTask = new Task();
        savedTask.setId(100L);
        savedTask.setTitle("Task Title");
        savedTask.setDescription("Task Desc");
        savedTask.setProject(project);
        savedTask.setAssignee(assignee);
        savedTask.setStatus(TaskStatus.TODO);

        when(taskRepository.save(any(Task.class))).thenReturn(savedTask);

        TaskResponse response = taskService.createTask(createTaskRequest);

        assertNotNull(response);
        assertEquals("Task Title", response.getTitle());

        verify(taskRepository).save(any(Task.class));
        verify(applicationEventPublisher).publishEvent(any(TaskCreatedEvent.class));

    }

    @Test
    void updateTask_updateTaskByAdmin() {
        mockCurrentUser(email);

        User currentUser = new User();
        currentUser.setId(1L);
        currentUser.setRole(Role.ADMIN);

        Project project = new Project();
        project.setId(1L);
        project.setName("test");

        Task task = new Task();
        task.setId(100L);
        task.setTitle("Task Title");
        task.setDescription("Task Desc");
        task.setStatus(TaskStatus.TODO);

        User assignee = new User();
        assignee.setId(2L);
        assignee.setName("assignee");

        task.setAssignee(assignee);
        task.setProject(project);

        UpdateTaskRequest updateTaskRequest = new UpdateTaskRequest();
        updateTaskRequest.setDescription("Updated Desc");
        updateTaskRequest.setTitle("Updated Title");
        updateTaskRequest.setAssigneeId(assignee.getId());

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(currentUser));
        when(taskRepository.findById(100L)).thenReturn(Optional.of(task));
        when(userRepository.findById(2L)).thenReturn(Optional.of(assignee));

        when(taskRepository.save(any(Task.class))).thenReturn(task);

        TaskResponse response = taskService.updateTask(100L, updateTaskRequest);

        assertNotNull(response);
        assertEquals("Updated Title", response.getTitle());
        assertEquals("Updated Desc", response.getDescription());
        assertEquals(2L, response.getAssigneeId());
        verify(taskRepository).save(any(Task.class));
    }

    @Test
    void updateTask_updateTaskByNonAdminNonAssignee() {
        mockCurrentUser(email);

        User currentUser = new User();
        currentUser.setId(1L);
        currentUser.setRole(Role.USER);

        Project project = new Project();
        project.setId(1L);
        project.setName("test");

        Task task = new Task();
        task.setId(100L);
        task.setTitle("Task Title");
        task.setDescription("Task Desc");
        task.setStatus(TaskStatus.TODO);

        task.setAssignee(currentUser);
        task.setProject(project);

        UpdateTaskRequest updateTaskRequest = new UpdateTaskRequest();
        updateTaskRequest.setDescription("Updated Desc");
        updateTaskRequest.setTitle("Updated Title");

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(currentUser));
        when(taskRepository.findById(100L)).thenReturn(Optional.of(task));

        when(taskRepository.save(any(Task.class))).thenReturn(task);

        TaskResponse response = taskService.updateTask(100L, updateTaskRequest);

        assertNotNull(response);
        assertEquals("Updated Title", response.getTitle());
        assertEquals("Updated Desc", response.getDescription());
        assertEquals(1L, response.getAssigneeId());
        verify(taskRepository).save(any(Task.class));

    }

    @Test
    void getTaskById_shouldReturnTaskSuccessfully() {
        mockCurrentUser(email);

        User currentUser = new User();
        currentUser.setId(1L);

        Project project = new Project();
        project.setId(1L);
        project.setName("test");

        Task task = new Task();
        task.setId(100L);
        task.setTitle("Task Title");
        task.setDescription("Task Desc");
        task.setStatus(TaskStatus.TODO);
        task.setAssignee(currentUser);
        task.setProject(project);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(currentUser));
        when(taskRepository.findById(100L)).thenReturn(Optional.of(task));

        TaskResponse response = taskService.getTaskById(100L);

        assertNotNull(response);
    }

    @Test
    void deleteTask_shouldDeleteTaskSuccessfully() {
        mockCurrentUser(email);

        User currentUser = new User();
        currentUser.setId(1L);
        currentUser.setRole(Role.USER);

        Project project = new Project();
        project.setId(1L);
        project.setName("test");

        Task task = new Task();
        task.setId(100L);
        task.setTitle("Task Title");
        task.setDescription("Task Desc");
        task.setStatus(TaskStatus.TODO);
        task.setAssignee(currentUser);
        task.setProject(project);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(currentUser));
        when(taskRepository.findById(100L)).thenReturn(Optional.of(task));
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        taskService.deleteTask(100L);

        assertTrue(task.isDeleted());
        assertEquals(currentUser.getId(), task.getDeletedBy());

        verify(taskRepository).save(any(Task.class));
    }

    private void mockCurrentUser(String email){
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn(email);

        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);

        SecurityContextHolder.setContext(securityContext);
    }
}


