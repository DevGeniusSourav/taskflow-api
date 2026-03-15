package com.sourav.taskflow.dto;

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

    @NotBlank(message = "Title cannot be blank!")
    private String title;

    private String description;

    @NotNull(message = "Project Id is required!")
    private Long projectId;

    @NotNull(message = "User Id is required!")
    private Long assigneeId;
}
