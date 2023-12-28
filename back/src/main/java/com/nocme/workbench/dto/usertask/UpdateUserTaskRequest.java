package com.nocme.workbench.dto.usertask;

import java.time.LocalDateTime;

public record UpdateUserTaskRequest(
        Long id,
        LocalDateTime assignmentDate,
        Long assignerId,
        Long userId,
        Long taskId,
        Boolean is_task_completed

) { }