package com.mediaworkbench.workbench.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mediaworkbench.workbench.dto.usertask.*;
import com.mediaworkbench.workbench.model.UserTask;
import com.mediaworkbench.workbench.model.User;
import com.mediaworkbench.workbench.model.Task;
import com.mediaworkbench.workbench.model.UserTaskStatus;
import com.mediaworkbench.workbench.repository.IUserTaskRepository;
import com.mediaworkbench.workbench.repository.IUserRepository;
import com.mediaworkbench.workbench.repository.ITaskRepository;
import com.mediaworkbench.workbench.service.IUserTaskService;
import com.mediaworkbench.workbench.utils.exceptions.CustomNotFoundException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserTaskService implements IUserTaskService {

    private static final Logger LOGGER = Logger.getLogger(UserTaskService.class);

    @Autowired
    private IUserTaskRepository userTaskRepository;
    @Autowired
    private IUserRepository userRepository;
    @Autowired
    private ITaskRepository taskRepository;
    @Autowired
    private ObjectMapper mapper;

    @Override
    public void insertUserTask(CreateUserTaskRequest createUserTaskRequest) {
        UserTask userTask = new UserTask();
        userTask.setAssignmentDate(createUserTaskRequest.assignmentDate());
        userTask.setUserTaskStatus(UserTaskStatus.ASSIGNED); // Set default status as ASSIGNED

        User assigner = userRepository.findById(createUserTaskRequest.assignerId())
                .orElseThrow(() -> new CustomNotFoundException("Assigner id [" + createUserTaskRequest.assignerId() + "] not found"));
        User user = userRepository.findById(createUserTaskRequest.userId())
                .orElseThrow(() -> new CustomNotFoundException("User id [" + createUserTaskRequest.userId() + "] not found"));
        Task task = taskRepository.findById(createUserTaskRequest.taskId())
                .orElseThrow(() -> new CustomNotFoundException("Task id [" + createUserTaskRequest.taskId() + "] not found"));

        userTask.setAssigner(assigner);
        userTask.setUser(user);
        userTask.setTask(task);

        userTaskRepository.save(userTask);
        LOGGER.info("New user task created [" + task.getName() + "] for user [" + user.getName() + " " + user.getSurname() + "]");
    }

    @Override
    public List<UserTaskResponse> selectAllUserTask() {
        List<UserTask> userTasks = userTaskRepository.findAll();
        return userTasks.stream().map(userTask -> new UserTaskResponse(
                userTask.getId(),
                userTask.getAssignmentDate(),
                userTask.getUser().getName(), // User's name to whom the task is assigned
                userTask.getUser().getSurname(), // User's surname to whom the task is assigned
                userTask.getTask().getName(), // Task's name
                userTask.getUserTaskStatus().toString(), // User task status
                userTask.getAssigner().getName(), // Assigner's name
                userTask.getAssigner().getSurname() // Assigner's surname
        )).collect(Collectors.toList());
    }

    @Override
    public UserTaskResponse selectUserTaskByID(Long id) {
        UserTask userTask = userTaskRepository.findById(id)
                .orElseThrow(() -> new CustomNotFoundException("UserTask id [" + id + "] not found"));
        return new UserTaskResponse(
                userTask.getId(),
                userTask.getAssignmentDate(),
                userTask.getUser().getName(),       // User's name to whom the task is assigned
                userTask.getUser().getSurname(),    // User's surname to whom the task is assigned
                userTask.getTask().getName(),       // Task's name
                userTask.getUserTaskStatus().toString(), // User task status
                userTask.getAssigner().getName(),   // Assigner's name
                userTask.getAssigner().getSurname() // Assigner's surname
        );
    }

    @Override
    public void updateUserTaskByID(UpdateUserTaskRequest updateUserTaskRequest) {
        UserTask userTask = userTaskRepository.findById(updateUserTaskRequest.id())
                .orElseThrow(() -> new CustomNotFoundException("UserTask id [" + updateUserTaskRequest.id() + "] not found"));

        userTask.setAssignmentDate(updateUserTaskRequest.assignmentDate());
        userTask.setUserTaskStatus(updateUserTaskRequest.userTaskStatus());

        if (updateUserTaskRequest.assignerId() != null) {
            User assigner = userRepository.findById(updateUserTaskRequest.assignerId())
                    .orElseThrow(() -> new CustomNotFoundException("Assigner id [" + updateUserTaskRequest.assignerId() + "] not found"));
            userTask.setAssigner(assigner);
        }
        if (updateUserTaskRequest.userId() != null) {
            User user = userRepository.findById(updateUserTaskRequest.userId())
                    .orElseThrow(() -> new CustomNotFoundException("User id [" + updateUserTaskRequest.userId() + "] not found"));
            userTask.setUser(user);
        }
        if (updateUserTaskRequest.taskId() != null) {
            Task task = taskRepository.findById(updateUserTaskRequest.taskId())
                    .orElseThrow(() -> new CustomNotFoundException("Task id [" + updateUserTaskRequest.taskId() + "] not found"));
            userTask.setTask(task);
        }

        userTaskRepository.save(userTask);
        LOGGER.info("UserTask id [" + updateUserTaskRequest.id() + "] updated successfully!");
    }

    @Override
    @Transactional
    public void cancelUserTaskByID(Long id) {
        LOGGER.info("Attempting to cancel userTask with id: " + id);

        UserTask userTask = userTaskRepository.findById(id)
                .orElseThrow(() -> new CustomNotFoundException("UserTask id [" + id + "] not found"));

        // Update UserTask status to CANCELED
        userTask.setUserTaskStatus(UserTaskStatus.CANCELED);

        userTaskRepository.save(userTask);
        LOGGER.info("UserTask with id [" + id + "] successfully updated to CANCELED.");
    }

}
