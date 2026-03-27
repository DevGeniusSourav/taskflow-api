package com.sourav.taskflow.dto.projects;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProjectResponse {

    private Long id;

    private String name;

    private String description;

    private Long ownerId;

    private String ownerName;

    private LocalDateTime createdAt;
}
