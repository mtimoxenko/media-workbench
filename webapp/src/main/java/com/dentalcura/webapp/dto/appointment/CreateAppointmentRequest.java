package com.dentalcura.webapp.dto.appointment;

import com.dentalcura.webapp.model.Comment;
import com.dentalcura.webapp.model.Task;

public record CreateAppointmentRequest(
        String date,
        Task task,
        Comment comment
) { }
