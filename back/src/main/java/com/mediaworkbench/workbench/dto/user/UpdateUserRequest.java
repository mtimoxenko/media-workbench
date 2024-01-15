package com.mediaworkbench.workbench.dto.user;

import com.mediaworkbench.workbench.model.UserRoleStatus;
import com.mediaworkbench.workbench.model.UserShiftStatus;

public record UpdateUserRequest(
        Long id,
        String name,
        String surname,
        String email,
        String password,
        UserShiftStatus shift,
        UserRoleStatus role,
        Boolean isAdmin
) { }