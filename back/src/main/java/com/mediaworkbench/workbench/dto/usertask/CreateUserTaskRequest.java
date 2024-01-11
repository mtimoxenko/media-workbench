package com.mediaworkbench.workbench.dto.usertask;

import java.time.LocalDateTime;

public record CreateUserTaskRequest(
        Long assignerId,
        Long userId,
        Long taskId

) { }