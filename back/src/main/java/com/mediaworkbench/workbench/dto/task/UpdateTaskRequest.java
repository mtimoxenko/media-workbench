
package com.mediaworkbench.workbench.dto.task;

import com.mediaworkbench.workbench.model.TaskStatus;

public record UpdateTaskRequest(
        Long id,
        String name,
        String description,
        TaskStatus status // Add this line for the status
) { }