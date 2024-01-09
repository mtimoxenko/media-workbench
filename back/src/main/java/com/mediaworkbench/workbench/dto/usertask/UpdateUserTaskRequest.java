package com.mediaworkbench.workbench.dto.usertask;

import com.mediaworkbench.workbench.model.UserTaskStatus;

public record UpdateUserTaskRequest(
        Long id,
        UserTaskStatus userTaskStatus
) { }
