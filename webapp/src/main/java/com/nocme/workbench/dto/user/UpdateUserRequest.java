package com.nocme.workbench.dto.user;

public record UpdateUserRequest(
        String name,
        String surname,
        String email,
        String password,
        Boolean admin
) { }