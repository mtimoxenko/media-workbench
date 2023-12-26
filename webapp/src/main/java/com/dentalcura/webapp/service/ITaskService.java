package com.dentalcura.webapp.service;

import com.dentalcura.webapp.dto.task.CreateTaskRequest;
import com.dentalcura.webapp.dto.task.UpdateTaskRequest;
import com.dentalcura.webapp.dto.task.TaskResponse;

import java.util.List;


public interface ITaskService {

    void insertTask(CreateTaskRequest createTaskRequest);
    List<TaskResponse> selectAllTask();
    TaskResponse selectTaskByID(Long id);
    void updateTaskByID(Long id, UpdateTaskRequest updateTaskRequest);
    void deleteTaskByID(Long id);

}
