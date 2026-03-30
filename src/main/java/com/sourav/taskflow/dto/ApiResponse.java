package com.sourav.taskflow.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ApiResponse<T> {
    private T data;
    private String message;

    public ApiResponse(String message) {
        this.message = message;
    }
}
