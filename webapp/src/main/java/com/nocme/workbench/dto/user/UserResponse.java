package com.dentalcura.webapp.dto.user;

public record UserResponse(
        Long id,
        String name,
        String surname,
        String email,
        Boolean admin
) { }
