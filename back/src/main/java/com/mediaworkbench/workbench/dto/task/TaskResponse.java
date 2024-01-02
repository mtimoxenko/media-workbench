package com.mediaworkbench.workbench.dto.task;

import com.mediaworkbench.workbench.dto.comment.CommentResponse;
import com.mediaworkbench.workbench.model.TaskStatus;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


public record TaskResponse(
        Long id,
        String name,
        String description,
        TaskStatus status, // Status of the task
        String creatorName, // Name of the task creator
        String creatorSurname, // Surname of the task creator
        List<CommentResponse> comments // List of comments associated with the task
) { }
