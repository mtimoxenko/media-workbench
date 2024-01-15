package com.mediaworkbench.workbench.dto.user;

import com.mediaworkbench.workbench.model.UserRoleStatus;
import com.mediaworkbench.workbench.model.ShiftStatus;

public record CreateUserRequest(
        String name,
        String surname,
        String email,
        String password,
        ShiftStatus shift,
        UserRoleStatus role
) { }
