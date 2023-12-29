package com.mediaworkbench.workbench.dto.task;

public record UpdateTaskRequest(
        Long id,
        String name,
        String description,
        Boolean isCompleted
) { }