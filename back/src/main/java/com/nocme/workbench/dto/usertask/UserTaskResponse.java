package com.nocme.workbench.dto.usertask;

import java.time.LocalDateTime;

public record UserTaskResponse(
        Long id,
        LocalDateTime assignmentDate,
        Long assignerId,
        String assignerName,  // Name of the assigner
        Long userId,
        String userName,      // Name of the user
        Long taskId,
        String taskName,      // Name of the task
        Boolean isTaskCompleted
) {}