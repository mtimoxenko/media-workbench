package com.nocme.workbench.service;

import com.nocme.workbench.dto.task.CreateTaskRequest;
import com.nocme.workbench.dto.task.UpdateTaskRequest;
import com.nocme.workbench.dto.task.TaskResponse;

import java.util.List;


public interface ITaskService {

    void insertTask(CreateTaskRequest createTaskRequest);
    List<TaskResponse> selectAllTask();
    TaskResponse selectTaskByID(Long id);
    void updateTaskByID(UpdateTaskRequest updateTaskRequest);
    void deleteTaskByID(Long id);

}
