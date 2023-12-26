package com.dentalcura.webapp.dto.task;

public record CreateTaskRequest(
        String name,
        String surname,
        Integer licenseNumber
) { }
