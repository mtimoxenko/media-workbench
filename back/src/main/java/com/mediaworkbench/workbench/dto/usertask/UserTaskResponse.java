package com.mediaworkbench.workbench.dto.usertask;

import java.time.LocalDateTime;

public record UserTaskResponse(
        Long id,
        LocalDateTime assignmentDate,
        String userName,           // Name of the user to whom the task is assigned
        String userSurname,        // Surname of the user to whom the task is assigned
        Long taskId,                // Include task's id
        String taskName,           // Include task's name
        String userTaskStatus,     // Updated to represent status as a string
        String assignerName,       // Name of the user who assigned the task
        String assignerSurname     // Surname of the user who assigned the task
) {}
