package com.mediaworkbench.workbench.dto.user;

public record LoginUserResponse(
        int token,
        String userName,
        long userId
) { }
