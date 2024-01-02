package com.mediaworkbench.workbench.service;

import com.mediaworkbench.workbench.dto.usertask.CreateUserTaskRequest;
import com.mediaworkbench.workbench.dto.usertask.UpdateUserTaskRequest;
import com.mediaworkbench.workbench.dto.usertask.UserTaskResponse;

import java.util.List;


public interface IUserTaskService {

    void insertUserTask(CreateUserTaskRequest createUserTaskRequest);
    List<UserTaskResponse> selectAllUserTask();
    UserTaskResponse selectUserTaskByID(Long id);
    void updateUserTaskByID(UpdateUserTaskRequest updateUserTaskRequest);
    void cancelUserTaskByID(Long id);

}
