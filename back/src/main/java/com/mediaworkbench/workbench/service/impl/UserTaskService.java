package com.mediaworkbench.workbench.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mediaworkbench.workbench.dto.usertask.CreateUserTaskRequest;
import com.mediaworkbench.workbench.dto.usertask.UpdateUserTaskRequest;
import com.mediaworkbench.workbench.dto.usertask.UserTaskResponse;
import com.mediaworkbench.workbench.repository.ITaskRepository;
import com.mediaworkbench.workbench.repository.IUserRepository;
import com.mediaworkbench.workbench.repository.IUserTaskRepository;
import com.mediaworkbench.workbench.model.UserTask;
import com.mediaworkbench.workbench.model.User;
import com.mediaworkbench.workbench.model.Task;
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
        userTask.setIsTaskCompleted(false); // Default value for new user task

        // Fetch the assigner, user and task entities by ID
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
        LOGGER.info("New user task created for task [" + task.getName() + "] and user [" + user.getName() + "]");
    }

    @Override
    public List<UserTaskResponse> selectAllUserTask() {
        List<UserTask> userTasks = userTaskRepository.findAll();
        return userTasks.stream().map(userTask -> new UserTaskResponse(
                userTask.getId(),
                userTask.getAssignmentDate(),
                userTask.getAssigner().getName(),
                userTask.getAssigner().getSurname(), // Assigner's surname
                userTask.getTask().getName(),        // Task's name
                userTask.getIsTaskCompleted()        // Task completion status
        )).collect(Collectors.toList());
    }

    @Override
    public UserTaskResponse selectUserTaskByID(Long id) {
        UserTask userTask = userTaskRepository.findById(id)
                .orElseThrow(() -> new CustomNotFoundException("UserTask id [" + id + "] not found"));
        return new UserTaskResponse(
                userTask.getId(),
                userTask.getAssignmentDate(),
                userTask.getAssigner().getName(),
                userTask.getAssigner().getSurname(), // Assigner's surname
                userTask.getTask().getName(),        // Task's name
                userTask.getIsTaskCompleted()        // Task completion status
        );
    }


    @Override
    public void updateUserTaskByID(UpdateUserTaskRequest updateUserTaskRequest) {
        // Fetch the userTask by the ID provided in the request
        UserTask userTask = userTaskRepository.findById(updateUserTaskRequest.id())
                .orElseThrow(() -> new CustomNotFoundException("UserTask id [" + updateUserTaskRequest.id() + "] not found"));

        // Update fields
        userTask.setAssignmentDate(updateUserTaskRequest.assignmentDate());
        userTask.setIsTaskCompleted(updateUserTaskRequest.is_task_completed());

        // Update assigner, user, and task only if their IDs are provided in the request
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

        // Save the updated userTask
        userTaskRepository.save(userTask);
        LOGGER.info("UserTask id [" + updateUserTaskRequest.id() + "] updated successfully!");
    }


    @Override
    @Transactional
    public void deleteUserTaskByID(Long id) {
        UserTask userTask = userTaskRepository.findById(id)
                .orElseThrow(() -> new CustomNotFoundException("UserTask id [" + id + "] not found"));
        userTaskRepository.delete(userTask);
        LOGGER.info("UserTask with id [" + id + "] successfully deleted.");
    }

}
