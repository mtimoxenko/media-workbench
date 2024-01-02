package com.mediaworkbench.workbench.dto.usertask;

import com.mediaworkbench.workbench.model.UserTaskStatus;
import java.time.LocalDateTime;

public record UpdateUserTaskRequest(
        Long id,
        LocalDateTime assignmentDate,
        Long assignerId,
        Long userId,
        Long taskId,
        UserTaskStatus userTaskStatus // Use enum type for status
) { }
