package com.mediaworkbench.workbench.dto.usertask;

import java.time.LocalDateTime;

public record CreateUserTaskRequest(
        LocalDateTime assignmentDate,
        Long assignerId,
        Long userId,
        Long taskId

) { }