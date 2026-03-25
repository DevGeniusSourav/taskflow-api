package com.sourav.taskflow.listener;

import com.sourav.taskflow.event.tasks.TaskCreatedEvent;
import com.sourav.taskflow.event.tasks.TaskDeletedEvent;
import com.sourav.taskflow.event.tasks.TaskUpdatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@Slf4j
public class TaskEventListener {

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Async
    public void handleTaskCreated(TaskCreatedEvent event) {
        log.info("TaskCreatedEvent: with id = " + event.getTaskId() + ", AssigneeId: " + event.getAssigneeId());
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleTaskUpdated(TaskUpdatedEvent event) {
        log.info("TaskUpdatedEvent: with id = " + event.getTaskId() + ", AssigneeId: " + event.getAssigneeId());
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleTaskDeleted(TaskDeletedEvent event) {
        log.info("TaskDeletedEvent: with id = " + event.getTaskId() + ", AssigneeId: " + event.getAssigneeId());
    }

}
