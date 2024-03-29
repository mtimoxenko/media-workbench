package com.mediaworkbench.workbench.dto.schedule;

import java.time.LocalDate;
import com.mediaworkbench.workbench.model.UserRoleStatus;

public record ScheduleResponse(
        Long id,
        Long userId,
        LocalDate date,
        UserRoleStatus role,
        Boolean isWorkingDay
) { }
