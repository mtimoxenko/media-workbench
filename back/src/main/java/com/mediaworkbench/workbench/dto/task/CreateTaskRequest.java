package com.mediaworkbench.workbench.dto.task;

import com.mediaworkbench.workbench.model.ShiftStatus;
import com.mediaworkbench.workbench.model.TaskCategory;

public record CreateTaskRequest(
        String name,
        String description,
        Long creatorId,
        TaskCategory category,
        ShiftStatus shiftStatus
) { }
