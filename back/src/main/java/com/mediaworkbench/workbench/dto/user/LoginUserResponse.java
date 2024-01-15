package com.mediaworkbench.workbench.dto.user;

import com.mediaworkbench.workbench.model.UserRoleStatus;
import com.mediaworkbench.workbench.model.ShiftStatus;

public record LoginUserResponse(
        int token,
        String userName,
        long userId,
        ShiftStatus shift,
        UserRoleStatus role
) { }
