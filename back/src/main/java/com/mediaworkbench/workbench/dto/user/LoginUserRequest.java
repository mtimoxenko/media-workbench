package com.mediaworkbench.workbench.dto.user;

public record LoginUserRequest(
        String email,
        String password
) { }
