package com.dentalcura.webapp.service.impl;

import com.dentalcura.webapp.dto.appointment.CreateAppointmentRequest;
import com.dentalcura.webapp.dto.appointment.UpdateAppointmentRequest;
import com.dentalcura.webapp.model.UserTask;
import com.dentalcura.webapp.repository.ICommentRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserTaskServiceTest {

    @Autowired
    ICommentRepository appointmentRepository;

    @Autowired
    ObjectMapper mapper;

    UserTaskServiceTest() {
    }

    @Test
    void insertAppointment() {

        CreateAppointmentRequest createAppointmentRequest = new CreateAppointmentRequest(
                "test",
                null,
                null
        );

        UserTask userTask = mapper.convertValue(createAppointmentRequest, UserTask.class);

        appointmentRepository.save(userTask);

        List<UserTask> userTasks = appointmentRepository.findAll();

        boolean found = false;

        for (UserTask findUserTask : userTasks) {
            if (userTask.equals(findUserTask)) {
                found = true;
                break;
            }
        }

        assertTrue(found);
    }

    @Test
    void updateAppointmentByID() {
        CreateAppointmentRequest createAppointmentRequest = new CreateAppointmentRequest(
                "test",
                null,
                null
        );

        UserTask userTask = mapper.convertValue(createAppointmentRequest, UserTask.class);
        appointmentRepository.save(userTask);

        UpdateAppointmentRequest updateAppointmentRequest = new UpdateAppointmentRequest("newDate");

        UserTask userTaskUpdate = mapper.convertValue(updateAppointmentRequest, UserTask.class);
        userTaskUpdate.setId(2L);
        appointmentRepository.save(userTaskUpdate);

        Optional<UserTask> optionalAppointment = appointmentRepository.findById(2L);
        UserTask newUserTask = null;

        if(optionalAppointment.isPresent())
            newUserTask = optionalAppointment.get();


        assert newUserTask != null;
        assertEquals(userTaskUpdate.getDate(), newUserTask.getDate());
    }
    

    @Test
    void deleteAppointmentByID() {
        
        CreateAppointmentRequest createAppointmentRequest = new CreateAppointmentRequest(
                "dateToDelete",
                null,
                null
        );

        UserTask userTaskToDelete = mapper.convertValue(createAppointmentRequest, UserTask.class);
        appointmentRepository.save(userTaskToDelete);

        appointmentRepository.deleteById(6L);

        List<UserTask> userTasks = appointmentRepository.findAll();

        boolean found = false;

        for (UserTask findUserTask : userTasks) {
            if (findUserTask.equals(userTaskToDelete)) {
                System.out.println(findUserTask);
                found = true;
                break;
            }
        }

        assertFalse(found);
        
    }
}