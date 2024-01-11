package com.mediaworkbench.workbench.dto.comment;

public record CreateCommentRequest(
        String text,
        Long userId,
        Long taskId
) { }
