package com.mediaworkbench.workbench.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mediaworkbench.workbench.dto.task.CreateTaskRequest;
import com.mediaworkbench.workbench.dto.task.TaskResponse;
import com.mediaworkbench.workbench.dto.task.UpdateTaskRequest;
import com.mediaworkbench.workbench.repository.ITaskRepository;
import com.mediaworkbench.workbench.repository.IUserRepository;
import com.mediaworkbench.workbench.model.Task;
import com.mediaworkbench.workbench.model.User;
import com.mediaworkbench.workbench.service.ITaskService;
import com.mediaworkbench.workbench.utils.exceptions.CustomDatabaseException;
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

@Getter @Setter
@Service
public class TaskService implements ITaskService {

    private final static Logger LOGGER = Logger.getLogger(TaskService.class);

    @Autowired
    private ITaskRepository taskRepository;
    @Autowired
    private IUserRepository userRepository; // Add User repository


    @Autowired
    ObjectMapper mapper;

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
        LOGGER.info("New task was registered [" + task.getName() + "]");
    }

    @Override
    public List<TaskResponse> selectAllTask() {
        List<Task> tasks = taskRepository.findAll();
        List<TaskResponse> taskResponses = new ArrayList<>();

        for(Task task: tasks){
            taskResponses.add(mapper.convertValue(task, TaskResponse.class));
        }

        return taskResponses;
    }

    @Override
    public TaskResponse selectTaskByID(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new CustomNotFoundException("Task id [" + id + "] not found"));

        return mapper.convertValue(task, TaskResponse.class);
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
        existingTask.setIsCompleted(updateTaskRequest.isCompleted());

        // No need to update creation date or creator as they should remain constant after initial creation

        taskRepository.save(existingTask);
        LOGGER.info("Task id [" + updateTaskRequest.id() + "] successfully updated!");
    }

    @Override
    @Transactional
    public void deleteTaskByID(Long id) {
        LOGGER.info("Attempting to delete task with id: " + id);

        try {
            if (!taskRepository.existsById(id)) {
                LOGGER.warn("Attempted to delete non-existing task with id: " + id);
                throw new CustomNotFoundException("Task id [" + id + "] not found");
            }

            taskRepository.deleteById(id);
            LOGGER.info("Task with id [" + id + "] successfully deleted from the database.");
        } catch (DataAccessException e) {
            LOGGER.error("Database access error occurred while deleting task with id " + id, e);
            // Handle or rethrow as appropriate for your application
            throw new CustomDatabaseException("Failed to delete task due to database access error", e);
        } catch (Exception e) {
            LOGGER.error("Unexpected error occurred while deleting task with id " + id, e);
            // Handle or rethrow as appropriate for your application
            throw e;
        }
    }

}
