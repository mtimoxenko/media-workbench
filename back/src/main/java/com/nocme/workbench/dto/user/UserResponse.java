package com.nocme.workbench.dto.user;

import com.nocme.workbench.model.UserTask;

import java.util.List;

public record UserResponse(
        Long id,
        String name,
        String surname,
        String email,
        Boolean isAdmin,
        List<UserTask> assignedTasks
) { }
