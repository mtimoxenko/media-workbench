package com.dentalcura.webapp.dto.user;

public record UpdateUserRequest(
        String name,
        String surname,
        String email,
        String password,
        Boolean admin
) { }