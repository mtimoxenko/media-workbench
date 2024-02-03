package com.mediaworkbench.workbench.dto.schedule;

import java.time.LocalDate;
import com.mediaworkbench.workbench.model.UserRoleStatus;

public record UpdateScheduleRequest(
        Long id,
        UserRoleStatus role,
        Boolean isWorkingDay
) { }
