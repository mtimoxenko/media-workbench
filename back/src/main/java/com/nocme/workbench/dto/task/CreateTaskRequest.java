package com.nocme.workbench.dto.task;

import com.nocme.workbench.model.User;

public record CreateTaskRequest(
        String name,
        String description,
        Long creatorId
) { }
