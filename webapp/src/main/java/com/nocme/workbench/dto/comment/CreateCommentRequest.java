package com.dentalcura.webapp.dto.comment;

import com.dentalcura.webapp.model.Comment;
import com.dentalcura.webapp.model.Task;

public record CreateCommentRequest(
        String date,
        Task task,
        Comment comment
) { }
