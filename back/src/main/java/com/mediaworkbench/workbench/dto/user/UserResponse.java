package com.mediaworkbench.workbench.dto.user;

import com.mediaworkbench.workbench.dto.usertask.UserTaskResponse;

import java.util.List;

public record UserResponse(
        Long id,
        String name,
        String surname,
        String email,
        Boolean isAdmin,
        List<UserTaskResponse> assignedTasks
) { }
