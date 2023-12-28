package com.nocme.workbench.dto.comment;

public record UpdateCommentRequest(
        Long id,
        String date
) { }