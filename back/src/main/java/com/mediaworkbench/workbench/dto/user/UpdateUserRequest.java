package com.mediaworkbench.workbench.dto.user;

import com.mediaworkbench.workbench.model.ShiftStatus;
import com.mediaworkbench.workbench.model.UserRoleStatus;

public record UpdateUserRequest(
        Long id,
        String name,
        String surname,
        String email,
        String password,
        ShiftStatus shift,
        UserRoleStatus role,
        Boolean isAdmin
) { }