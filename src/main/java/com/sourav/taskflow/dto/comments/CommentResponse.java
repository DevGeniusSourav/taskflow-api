package com.sourav.taskflow.dto.comments;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommentResponse {

    private Long id;

    private String content;

    private Long taskId;

    private String taskName;

    private Long userId;

    private String userName;

    private LocalDateTime createdAt;
}
