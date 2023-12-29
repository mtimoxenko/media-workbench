package com.mediaworkbench.workbench.dto.comment;

public record CommentResponse(
        Long id,
        String text,
        Long taskId

) { }
