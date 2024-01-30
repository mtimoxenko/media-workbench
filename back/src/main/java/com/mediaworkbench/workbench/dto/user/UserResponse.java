package com.mediaworkbench.workbench.dto.user;

import com.mediaworkbench.workbench.dto.usertask.UserTaskResponse;
import com.mediaworkbench.workbench.model.UserRoleStatus;
import com.mediaworkbench.workbench.model.ShiftStatus;

import java.util.List;

public record UserResponse(
        Long id,
        String name,
        String surname,
        String email,
        ShiftStatus shift,
        UserRoleStatus role,
        Boolean isActive,
        List<UserTaskResponse> assignedTasks
) { }
