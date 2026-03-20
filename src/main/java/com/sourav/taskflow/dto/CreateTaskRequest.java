package com.sourav.taskflow.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateTaskRequest {

    @Schema(description = "Title of the task", example = "Fix login bug")
    @NotBlank(message = "Title cannot be blank!")
    private String title;

    @Schema(description = "Task description", example = "Users cannot login")
    private String description;

    @Schema(description = "Project ID", example = "1")
    @NotNull(message = "Project Id is required!")
    private Long projectId;

    @Schema(description = "Assignee user ID", example = "2")
    @NotNull(message = "User Id is required!")
    private Long assigneeId;
}
