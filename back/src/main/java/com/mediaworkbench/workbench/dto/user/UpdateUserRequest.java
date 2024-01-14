package com.mediaworkbench.workbench.dto.user;

import com.mediaworkbench.workbench.model.UserRolStatus;
import com.mediaworkbench.workbench.model.UserShiftStatus;

public record UpdateUserRequest(
        Long id,
        String name,
        String surname,
        String email,
        String password,
        UserShiftStatus shift,
        UserRolStatus rol,
        Boolean isAdmin
) { }