package com.nocme.workbench.dto.usertask;

import com.nocme.workbench.model.Task;
import com.nocme.workbench.model.User;

import java.time.LocalDateTime;

public record CreateUserTaskRequest(
        LocalDateTime assignmentDate,
        Long assignerId,
        Long userId,
        Long taskId

) { }