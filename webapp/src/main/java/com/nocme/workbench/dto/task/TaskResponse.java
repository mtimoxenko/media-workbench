package com.nocme.workbench.dto.task;

import java.util.List;

public record TaskResponse(
        Long id,
        String name,
        String description,
        Boolean isCompleted

) { }
