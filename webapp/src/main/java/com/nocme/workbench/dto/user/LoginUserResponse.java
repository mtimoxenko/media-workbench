package com.dentalcura.webapp.dto.user;

public record LoginUserResponse(
        int token,
        String userName
) { }
