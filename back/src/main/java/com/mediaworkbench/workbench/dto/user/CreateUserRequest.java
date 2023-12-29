package com.mediaworkbench.workbench.dto.user;

public record CreateUserRequest(
        String name,
        String surname,
        String email,
        String password,
        Boolean isAdmin
) { }
