package com.dentalcura.webapp.dto.user;

public record LoginUserRequest(
        String email,
        String password
) { }
