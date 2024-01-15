package com.mediaworkbench.workbench.dto.user;

import com.mediaworkbench.workbench.dto.usertask.UserTaskResponse;
import com.mediaworkbench.workbench.model.UserRoleStatus;
import com.mediaworkbench.workbench.model.UserShiftStatus;

import java.util.List;

public record UserResponse(
        Long id,
        String name,
        String surname,
        String email,
        UserShiftStatus shift,
        UserRoleStatus role,
        Boolean isAdmin,
        List<UserTaskResponse> assignedTasks
) { }
