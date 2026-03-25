package com.sourav.taskflow.event.comments;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CommentDeletedEvent {
    private final Long commentId;
    private final Long userId;
}
