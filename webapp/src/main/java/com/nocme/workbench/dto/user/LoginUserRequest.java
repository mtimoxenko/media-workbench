package com.nocme.workbench.dto.user;

public record LoginUserRequest(
        String email,
        String password
) { }
