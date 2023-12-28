package com.nocme.workbench.dto.task;

public record TaskResponse(
        Long id,
        String name,
        String description,
        Boolean isCompleted
) { }
