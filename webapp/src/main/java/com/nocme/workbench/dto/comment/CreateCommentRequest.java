package com.nocme.workbench.dto.comment;

import com.nocme.workbench.model.Comment;
import com.nocme.workbench.model.Task;

public record CreateCommentRequest(
        String date,
        Task task,
        Comment comment
) { }
