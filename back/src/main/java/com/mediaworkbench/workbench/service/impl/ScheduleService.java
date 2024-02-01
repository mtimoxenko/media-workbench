package com.mediaworkbench.workbench.service.impl;

import com.mediaworkbench.workbench.repository.IScheduleRepository;
import com.mediaworkbench.workbench.repository.IUserRepository;
import com.mediaworkbench.workbench.dto.schedule.CreateScheduleRequest;
import com.mediaworkbench.workbench.dto.schedule.UpdateScheduleRequest;
import com.mediaworkbench.workbench.dto.schedule.ScheduleResponse;
import com.mediaworkbench.workbench.model.Schedule;
import com.mediaworkbench.workbench.model.User;
import com.mediaworkbench.workbench.service.IScheduleService;
import com.mediaworkbench.workbench.utils.exceptions.CustomNotFoundException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ScheduleService implements IScheduleService {

    private final static Logger LOGGER = Logger.getLogger(ScheduleService.class);

    @Autowired
    private IScheduleRepository scheduleRepository;

    @Autowired
    private IUserRepository userRepository;

    @Override
    public void createSchedule(CreateScheduleRequest createScheduleRequest) {
        User user = userRepository.findById(createScheduleRequest.userId())
                .orElseThrow(() -> new CustomNotFoundException("User id [" + createScheduleRequest.userId() + "] not found"));

        Schedule schedule = new Schedule();
        schedule.setUser(user);
        schedule.setDate(createScheduleRequest.date());
        schedule.setDayOfWeek(createScheduleRequest.date().getDayOfWeek());
        schedule.setDayNumber(createScheduleRequest.date().getDayOfMonth());
        schedule.setIsWorkingDay(createScheduleRequest.isWorkingDay());
        schedule.setRole(createScheduleRequest.role());

        scheduleRepository.save(schedule);
        LOGGER.info("New schedule created for user [" + user.getId() + "]");
    }

    @Override
    public List<ScheduleResponse> getAllSchedules() {
        List<Schedule> schedules = scheduleRepository.findAll();
        return schedules.stream()
                .map(this::mapScheduleToScheduleResponse)
                .collect(Collectors.toList());
    }

    private ScheduleResponse mapScheduleToScheduleResponse(Schedule schedule) {
        return new ScheduleResponse(
                schedule.getId(),
                schedule.getUser().getId(),
                schedule.getDate(),
                schedule.getRole(),
                schedule.getIsWorkingDay()
        );
    }

    @Override
    public ScheduleResponse getScheduleById(Long id) {
        Schedule schedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new CustomNotFoundException("Schedule id [" + id + "] not found"));
        return mapScheduleToScheduleResponse(schedule);
    }

    @Override
    public void updateSchedule(UpdateScheduleRequest updateScheduleRequest) {
        Schedule schedule = scheduleRepository.findById(updateScheduleRequest.id())
                .orElseThrow(() -> new CustomNotFoundException("Schedule id [" + updateScheduleRequest.id() + "] not found"));

        schedule.setDate(updateScheduleRequest.date());
        schedule.setRole(updateScheduleRequest.role());
        schedule.setIsWorkingDay(updateScheduleRequest.isWorkingDay());

        scheduleRepository.save(schedule);
        LOGGER.info("Schedule id [" + updateScheduleRequest.id() + "] updated successfully!");
    }

    @Override
    @Transactional
    public void deleteSchedule(Long id) {
        if (!scheduleRepository.existsById(id)) {
            throw new CustomNotFoundException("Schedule id [" + id + "] not found");
        }
        scheduleRepository.deleteById(id);
        LOGGER.info("Schedule with id [" + id + "] successfully deleted.");
    }
}
