package com.nocme.workbench.dto.user;

public record UserResponse(
        Long id,
        String name,
        String surname,
        String email,
        Boolean isAdmin
) { }
