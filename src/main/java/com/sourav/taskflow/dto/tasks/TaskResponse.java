package com.sourav.taskflow.dto.tasks;

import com.sourav.taskflow.enums.TaskStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TaskResponse {

    private Long id;

    private String title;

    private String description;

    private TaskStatus status;

    private Long projectId;

    private String projectName;

    private Long assigneeId;

    private String assigneeName;

    private LocalDateTime createdAt;
}
