package com.sourav.taskflow.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TaskUpdatedEvent {
    private final Long taskId;
    private final Long assigneeId;
}
