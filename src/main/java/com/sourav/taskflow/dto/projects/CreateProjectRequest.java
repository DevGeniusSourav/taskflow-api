package com.sourav.taskflow.dto.projects;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateProjectRequest {

    @NotBlank(message = "Project Name Is Required!")
    private String name;

    @NotBlank(message = "Project Description Is Required!")
    private String description;
}
