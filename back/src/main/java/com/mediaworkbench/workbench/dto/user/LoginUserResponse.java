package com.mediaworkbench.workbench.dto.user;

import com.mediaworkbench.workbench.model.UserRolStatus;
import com.mediaworkbench.workbench.model.UserShiftStatus;

public record LoginUserResponse(
        int token,
        String userName,
        long userId,
        UserShiftStatus shift,
        UserRolStatus rol
) { }
