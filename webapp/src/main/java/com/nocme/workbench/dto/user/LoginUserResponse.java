package com.nocme.workbench.dto.user;

public record LoginUserResponse(
        int token,
        String userName
) { }
