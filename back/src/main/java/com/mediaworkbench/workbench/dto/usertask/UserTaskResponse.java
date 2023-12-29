// UserTaskResponse.java
package com.mediaworkbench.workbench.dto.usertask;

import java.time.LocalDateTime;

public record UserTaskResponse(
        Long id,
        LocalDateTime assignmentDate,
        String assignerName,      // Include assigner's name
        String assignerSurname,   // Include assigner's surname
        String taskName,          // Include task's name
        Boolean isTaskCompleted   // Task completion status
) {}
