package com.mediaworkbench.workbench.service;

import com.mediaworkbench.workbench.dto.schedule.CreateScheduleRequest;
import com.mediaworkbench.workbench.dto.schedule.UpdateScheduleRequest;
import com.mediaworkbench.workbench.dto.schedule.ScheduleResponse;
import com.mediaworkbench.workbench.model.Schedule;
import java.util.List;

public interface IScheduleService {

    // Method to create a new schedule
    void createSchedule(CreateScheduleRequest createScheduleRequest);

    // Method to retrieve all schedules
    List<ScheduleResponse> getAllSchedules();

    // Method to retrieve a single schedule by ID
    ScheduleResponse getScheduleById(Long id);

    // Method to update a schedule by ID
    void updateSchedule(UpdateScheduleRequest updateScheduleRequest);

    // Method to delete a schedule by ID
    void deleteSchedule(Long id);

}
