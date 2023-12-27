package com.dentalcura.webapp.dto.user;

public record CreateUserRequest(
        String name,
        String surname,
        String email,
        String password,
        Boolean admin
) { }
