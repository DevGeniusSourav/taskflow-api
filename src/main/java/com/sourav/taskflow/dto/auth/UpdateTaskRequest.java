package com.sourav.taskflow.dto.auth;

import com.sourav.taskflow.enums.TaskStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateTaskRequest {
    private String title;
    private String description;
    private TaskStatus status;
    private Long assigneeId;
}
