package com.nocme.workbench.dto.usertask;

public record UpdateUserTaskRequest(
        Long id,
        boolean is_task_completed

) { }