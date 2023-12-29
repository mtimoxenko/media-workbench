package com.mediaworkbench.workbench.dto.task;

public record CreateTaskRequest(
        String name,
        String description,
        Long creatorId
) { }
