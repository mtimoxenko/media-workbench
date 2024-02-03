
package com.mediaworkbench.workbench.service.impl;

import com.mediaworkbench.workbench.model.User;
import com.mediaworkbench.workbench.repository.IScheduleRepository;
import com.mediaworkbench.workbench.repository.IUserRepository;
import com.mediaworkbench.workbench.dto.schedule.CreateScheduleRequest;
import com.mediaworkbench.workbench.dto.schedule.UpdateScheduleRequest;
import com.mediaworkbench.workbench.dto.schedule.ScheduleResponse;
import com.mediaworkbench.workbench.model.Schedule;
import com.mediaworkbench.workbench.service.IScheduleService;
import com.mediaworkbench.workbench.utils.exceptions.CustomNotFoundException;
import com.mediaworkbench.workbench.utils.exceptions.CustomValidationException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
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
        validateSchedule(createScheduleRequest.userId(), createScheduleRequest.date());

        Schedule schedule = new Schedule();
        schedule.setUser(userRepository.findById(createScheduleRequest.userId())
                .orElseThrow(() -> new CustomNotFoundException("User id [" + createScheduleRequest.userId() + "] not found")));
        schedule.setDate(createScheduleRequest.date());
        schedule.setDayOfWeek(createScheduleRequest.date().getDayOfWeek());
        schedule.setDayNumber(createScheduleRequest.date().getDayOfMonth());
        schedule.setIsWorkingDay(createScheduleRequest.isWorkingDay());
        schedule.setRole(createScheduleRequest.role());

        try {
            scheduleRepository.save(schedule);
            LOGGER.info("New schedule created for user ID: " + createScheduleRequest.userId());
        } catch (DataIntegrityViolationException e) {
            throw new CustomValidationException("Duplicate schedule detected for user ID: " + createScheduleRequest.userId() + " on date: " + createScheduleRequest.date());
        }
    }

    @Override
    public List<ScheduleResponse> getAllSchedules() {
        return scheduleRepository.findAll().stream()
                .map(schedule -> new ScheduleResponse(
                        schedule.getId(),
                        schedule.getUser().getId(),
                        schedule.getDate(),
                        schedule.getRole(),
                        schedule.getIsWorkingDay()))
                .collect(Collectors.toList());
    }

    @Override
    public ScheduleResponse getScheduleById(Long id) {
        Schedule schedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new CustomNotFoundException("Schedule id [" + id + "] not found"));
        return new ScheduleResponse(
                schedule.getId(),
                schedule.getUser().getId(),
                schedule.getDate(),
                schedule.getRole(),
                schedule.getIsWorkingDay());
    }

    @Override
    public void updateSchedule(UpdateScheduleRequest updateScheduleRequest) {
        Schedule schedule = scheduleRepository.findById(updateScheduleRequest.id())
                .orElseThrow(() -> new CustomNotFoundException("Schedule id [" + updateScheduleRequest.id() + "] not found"));

        schedule.setIsWorkingDay(updateScheduleRequest.isWorkingDay());
        schedule.setRole(updateScheduleRequest.role());

        try {
            scheduleRepository.save(schedule);
            LOGGER.info("Schedule id [" + updateScheduleRequest.id() + "] updated successfully.");
        } catch (DataIntegrityViolationException e) {
            throw new CustomValidationException("Failed to update schedule due to data integrity violation.");
        }
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

    private void validateSchedule(Long userId, LocalDate date) {
        if (date.isBefore(LocalDate.now())) {
            throw new CustomValidationException("Schedule date must be in the future.");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomNotFoundException("User id [" + userId + "] not found"));

        scheduleRepository.findByUserAndDate(user, date).ifPresent(s -> {
            throw new CustomValidationException("A schedule for this user and date already exists.");
        });
    }

}
