
package com.mediaworkbench.workbench.dto.task;

import com.mediaworkbench.workbench.model.ShiftStatus;
import com.mediaworkbench.workbench.model.TaskStatus;

public record UpdateTaskRequest(
        Long id,
        TaskStatus status, // Add this line for the status
        ShiftStatus shiftStatus
) { }