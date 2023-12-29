package com.mediaworkbench.workbench.service;

import com.mediaworkbench.workbench.dto.task.CreateTaskRequest;
import com.mediaworkbench.workbench.dto.task.UpdateTaskRequest;
import com.mediaworkbench.workbench.dto.task.TaskResponse;

import java.util.List;


public interface ITaskService {

    void insertTask(CreateTaskRequest createTaskRequest);
    List<TaskResponse> selectAllTask();
    TaskResponse selectTaskByID(Long id);
    void updateTaskByID(UpdateTaskRequest updateTaskRequest);
    void deleteTaskByID(Long id);

}
