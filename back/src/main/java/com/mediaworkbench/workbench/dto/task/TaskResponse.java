package com.mediaworkbench.workbench.dto.task;

import com.mediaworkbench.workbench.model.TaskStatus;

public record TaskResponse(
        Long id,
        String name,
        String description,
        TaskStatus status, // Status of the task
        String creatorName, // Name of the task creator
        String creatorSurname // Surname of the task creator
) { }
