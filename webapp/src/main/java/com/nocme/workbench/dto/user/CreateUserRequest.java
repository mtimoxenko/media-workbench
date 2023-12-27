package com.nocme.workbench.dto.user;

public record CreateUserRequest(
        String name,
        String surname,
        String email,
        String password,
        Boolean admin
) { }
