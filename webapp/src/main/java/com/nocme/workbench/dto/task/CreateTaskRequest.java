package com.nocme.workbench.dto.task;

public record CreateTaskRequest(
        String name,
        String surname,
        Integer licenseNumber
) { }
