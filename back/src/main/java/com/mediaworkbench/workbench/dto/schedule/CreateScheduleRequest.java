package com.mediaworkbench.workbench.dto.schedule;

import java.time.LocalDate;
import com.mediaworkbench.workbench.model.UserRoleStatus;

public record CreateScheduleRequest(
        Long userId,
        LocalDate date,
        UserRoleStatus role,
        Boolean isWorkingDay
) { }
