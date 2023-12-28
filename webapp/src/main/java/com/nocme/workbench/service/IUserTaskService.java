package com.nocme.workbench.service;

import com.nocme.workbench.dto.usertask.CreateUserTaskRequest;
import com.nocme.workbench.dto.usertask.UpdateUserTaskRequest;
import com.nocme.workbench.dto.usertask.UserTaskResponse;

import java.util.List;


public interface IUserTaskService {

    void insertUserTask(CreateUserTaskRequest createUserTaskRequest);
    List<UserTaskResponse> selectAllUserTask();
    UserTaskResponse selectUserTaskByID(Long id);
    void updateUserTaskByID(UpdateUserTaskRequest updateUserTaskRequest);
    void deleteUserTaskByID(Long id);

}
