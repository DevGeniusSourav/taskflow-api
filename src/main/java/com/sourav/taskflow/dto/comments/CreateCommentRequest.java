package com.sourav.taskflow.dto.comments;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateCommentRequest {

    @NotBlank(message = "Content cannot be blank!")
    private String content;

    @NotNull(message = "Task ID is required!")
    private Long taskId;

}
