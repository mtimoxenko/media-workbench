package com.dentalcura.webapp.service.impl;

import com.dentalcura.webapp.dto.usertask.CreateUserTaskRequest;
import com.dentalcura.webapp.dto.usertask.UpdateUserTaskRequest;
import com.dentalcura.webapp.model.Task;
import com.dentalcura.webapp.repository.IUserTaskRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class TaskServiceTest {
    
    TaskServiceTest(){
    }

    @Autowired
    IUserTaskRepository patientRepository;

    @Autowired
    ObjectMapper mapper;

    @Test
    void insertPatient() {

        CreateUserTaskRequest createUserTaskRequest = new CreateUserTaskRequest(
                "testName",
                "testSurname",
                1111,
                "00/11/22",
                null
        );

        Task task = mapper.convertValue(createUserTaskRequest, Task.class);

        patientRepository.save(task);

        List<Task> tasks = patientRepository.findAll();

        boolean found = false;

        for (Task findTask : tasks) {
            if (task.getNiNumber().equals(findTask.getNiNumber())) {
                found = true;
                break;
            }
        }

        assertTrue(found);
    }

    @Test
    void updatePatientByID() {
        
        CreateUserTaskRequest createUserTaskRequest = new CreateUserTaskRequest(
                "testName",
                "testSurname",
                1111,
                "00/11/22",
                null
        );

        Task task = mapper.convertValue(createUserTaskRequest, Task.class);
        patientRepository.save(task);

        UpdateUserTaskRequest updateUserTaskRequest = new UpdateUserTaskRequest(
                "testNameUpdate",
                "testSurnameUpdate",
                null
        );

        Task taskUpdate = mapper.convertValue(updateUserTaskRequest, Task.class);
        taskUpdate.setId(1L);
        patientRepository.save(taskUpdate);

        Optional<Task> optionalPatient = patientRepository.findById(1L);
        Task newTask = null;

        if(optionalPatient.isPresent())
            newTask = optionalPatient.get();


        assert newTask != null;
        assertEquals(taskUpdate.getName(), newTask.getName());
    }

    @Test
    void deletePatientByID() {

        CreateUserTaskRequest createUserTaskRequest = new CreateUserTaskRequest(
                "testName",
                "testSurname",
                1111,
                "00/11/22",
                null
        );

        Task taskToDelete = mapper.convertValue(createUserTaskRequest, Task.class);
        patientRepository.save(taskToDelete);

        patientRepository.deleteById(4L);

        List<Task> tasks = patientRepository.findAll();

        boolean found = false;

        for (Task findTask : tasks) {
            if (findTask.equals(taskToDelete)) {
                found = true;
                break;
            }
        }

        assertFalse(found);
    }
}