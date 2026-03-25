package com.sourav.taskflow.listener;

import com.sourav.taskflow.event.comments.CommentCreatedEvent;
import com.sourav.taskflow.event.comments.CommentDeletedEvent;
import com.sourav.taskflow.event.comments.CommentUpdatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@Slf4j
public class CommentEventListener {

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Async
    public void handleCommentCreated(CommentCreatedEvent event) {
        log.info("CommentCreatedEvent: with ID = " + event.getCommentId() + ", UserID = " + event.getUserId());
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Async
    public void handleCommentUpdated(CommentUpdatedEvent event) {
        log.info("CommentUpdatedEvent: with ID = " + event.getCommentId() + ", UserID = " + event.getUserId());
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Async
    public void handleCommentDeleted(CommentDeletedEvent event) {
        log.info("CommentDeletedEvent: with ID = " + event.getCommentId() + ", UserID = " + event.getUserId());
    }


}
