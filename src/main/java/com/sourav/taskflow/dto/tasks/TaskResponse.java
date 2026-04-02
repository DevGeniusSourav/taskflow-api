package com.sourav.taskflow.dto.tasks;

import com.sourav.taskflow.enums.TaskStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TaskResponse implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

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
