package com.mediaworkbench.workbench.dto.comment;

import java.time.LocalDateTime;

public record CommentResponse(
        Long commentId,
        String commentText,
        LocalDateTime commentTimestamp,
        Long userId,
        String userName,
        String userSurname
) { }
