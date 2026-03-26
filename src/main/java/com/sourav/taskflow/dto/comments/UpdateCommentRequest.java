package com.sourav.taskflow.dto.comments;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
public class UpdateCommentRequest {

    @NotBlank(message = "Content cannot be blank!")
    private String content;
}
