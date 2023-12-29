package com.mediaworkbench.workbench.dto.comment;

public record UpdateCommentRequest(
        Long id,
        String text
) { }