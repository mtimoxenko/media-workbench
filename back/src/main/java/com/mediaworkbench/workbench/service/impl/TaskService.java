package com.mediaworkbench.workbench.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mediaworkbench.workbench.dto.task.CreateTaskRequest;
import com.mediaworkbench.workbench.dto.task.TaskResponse;
import com.mediaworkbench.workbench.dto.comment.CommentResponse;
import com.mediaworkbench.workbench.dto.task.UpdateTaskRequest;
import com.mediaworkbench.workbench.model.*;
import com.mediaworkbench.workbench.repository.ICommentRepository;
import com.mediaworkbench.workbench.repository.ITaskRepository;
import com.mediaworkbench.workbench.repository.IUserRepository;
import com.mediaworkbench.workbench.repository.IUserTaskRepository;
import com.mediaworkbench.workbench.service.ITaskService;
import com.mediaworkbench.workbench.utils.exceptions.CustomNotFoundException;
import jakarta.transaction.Transactional;
import lombok.Getter;
import lombok.Setter;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter @Setter
@Service
public class TaskService implements ITaskService {

    private final static Logger LOGGER = Logger.getLogger(TaskService.class);

    @Autowired
    private ITaskRepository taskRepository;
    @Autowired
    private IUserRepository userRepository;
    @Autowired
    private IUserTaskRepository userTaskRepository;
    @Autowired
    private ICommentRepository commentRepository;
    @Autowired
    private ObjectMapper mapper;



    @Override
    public void insertTask(CreateTaskRequest createTaskRequest) {
        Task task = new Task();
        task.setName(createTaskRequest.name());
        task.setDescription(createTaskRequest.description());

        // Fetch the creator user by ID and set it
        User creator = userRepository.findById(createTaskRequest.creatorId())
                .orElseThrow(() -> new CustomNotFoundException("User id [" + createTaskRequest.creatorId() + "] not found"));
        task.setCreator(creator);

        taskRepository.save(task);
        LOGGER.info("New task [" + task.getName() + "] was registered by [" + creator.getName() + " " + creator.getSurname() + "]");
    }


    @Override
    public List<TaskResponse> selectAllTask() {
        List<Task> tasks = taskRepository.findAll();
        return tasks.stream().map(this::mapTaskToTaskResponse).collect(Collectors.toList());
    }

    @Override
    public TaskResponse selectTaskByID(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new CustomNotFoundException("Task id [" + id + "] not found"));
        return mapTaskToTaskResponse(task);
    }


    @Override
    public void updateTaskByID(UpdateTaskRequest updateTaskRequest) {
        LOGGER.info("Attempting to update task with id: " + updateTaskRequest.id());

        if (!taskRepository.existsById(updateTaskRequest.id())) {
            throw new CustomNotFoundException("Task id [" + updateTaskRequest.id() + "] not found.");
        }

        Task existingTask = taskRepository.findById(updateTaskRequest.id())
                .orElseThrow(() -> new CustomNotFoundException("Task id [" + updateTaskRequest.id() + "] not found."));

        // Update task fields from request
        existingTask.setName(updateTaskRequest.name());
        existingTask.setDescription(updateTaskRequest.description());
        existingTask.setStatus(updateTaskRequest.status()); // Update the status of the task

        // No need to update creation date or creator as they should remain constant after initial creation

        taskRepository.save(existingTask);
        LOGGER.info("Task id [" + updateTaskRequest.id() + "] successfully updated!");
    }


    @Override
    @Transactional
    public void cancelTaskByID(Long id) {
        LOGGER.info("Attempting to cancel task with id: " + id);

        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new CustomNotFoundException("Task id [" + id + "] not found"));

        // Update Task status to CANCELED
        task.setStatus(TaskStatus.CANCELED);
        taskRepository.save(task);

        // Update all related UserTasks to CANCELED
        List<UserTask> userTasks = userTaskRepository.findByTaskId(id);
        userTasks.forEach(userTask -> {
            userTask.setUserTaskStatus(UserTaskStatus.CANCELED);
        });
        userTaskRepository.saveAll(userTasks);

        LOGGER.info("Task with id [" + id + "] and all related UserTasks successfully updated to CANCELED.");
    }


    private TaskResponse mapTaskToTaskResponse(Task task) {
        // Mapping comments manually
        List<CommentResponse> commentResponses = task.getComments().stream()
                .map(comment -> new CommentResponse(
                        comment.getId(),
                        comment.getText(),
                        comment.getTimestamp(),
                        comment.getUser().getId(),
                        comment.getUser().getName(),
                        comment.getUser().getSurname()))
                .collect(Collectors.toList());

        // Creating a new TaskResponse record with all fields
        return new TaskResponse(
                task.getId(),
                task.getName(),
                task.getDescription(),
                task.getStatus(),
                task.getCreator().getName(),
                task.getCreator().getSurname(),
                commentResponses);
    }



}
