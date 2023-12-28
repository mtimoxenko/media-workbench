package com.nocme.workbench.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nocme.workbench.dto.usertask.*;
import com.nocme.workbench.model.UserTask;
import com.nocme.workbench.model.User;
import com.nocme.workbench.model.Task;
import com.nocme.workbench.repository.IUserTaskRepository;
import com.nocme.workbench.repository.IUserRepository;
import com.nocme.workbench.repository.ITaskRepository;
import com.nocme.workbench.service.IUserTaskService;
import com.nocme.workbench.utils.exceptions.CustomNotFoundException;
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
        return userTasks.stream().map(userTask -> {
            Long assignerId = userTask.getAssigner() != null ? userTask.getAssigner().getId() : null;
            String assignerName = userTask.getAssigner() != null ? userTask.getAssigner().getName() : null;
            Long userId = userTask.getUser() != null ? userTask.getUser().getId() : null;
            String userName = userTask.getUser() != null ? userTask.getUser().getName() : null;
            Long taskId = userTask.getTask() != null ? userTask.getTask().getId() : null;
            String taskName = userTask.getTask() != null ? userTask.getTask().getName() : null;
            Boolean isTaskCompleted = userTask.getIsTaskCompleted();

            return new UserTaskResponse(
                    userTask.getId(),
                    userTask.getAssignmentDate(),
                    assignerId,
                    assignerName,
                    userId,
                    userName,
                    taskId,
                    taskName,
                    isTaskCompleted
            );
        }).collect(Collectors.toList());
    }


    @Override
    public UserTaskResponse selectUserTaskByID(Long id) {
        UserTask userTask = userTaskRepository.findById(id)
                .orElseThrow(() -> new CustomNotFoundException("UserTask id [" + id + "] not found"));

        Long assignerId = userTask.getAssigner() != null ? userTask.getAssigner().getId() : null;
        String assignerName = userTask.getAssigner() != null ? userTask.getAssigner().getName() : null;
        Long userId = userTask.getUser() != null ? userTask.getUser().getId() : null;
        String userName = userTask.getUser() != null ? userTask.getUser().getName() : null;
        Long taskId = userTask.getTask() != null ? userTask.getTask().getId() : null;
        String taskName = userTask.getTask() != null ? userTask.getTask().getName() : null;
        Boolean isTaskCompleted = userTask.getIsTaskCompleted();

        return new UserTaskResponse(
                userTask.getId(),
                userTask.getAssignmentDate(),
                assignerId,
                assignerName,
                userId,
                userName,
                taskId,
                taskName,
                isTaskCompleted
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
