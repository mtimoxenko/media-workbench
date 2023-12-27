package com.dentalcura.webapp.service;

import com.dentalcura.webapp.dto.usertask.CreateUserTaskRequest;
import com.dentalcura.webapp.dto.usertask.UpdateUserTaskRequest;
import com.dentalcura.webapp.dto.usertask.UserTaskResponse;

import java.util.List;


public interface IUserTaskService {

    void insertUserTask(CreateUserTaskRequest createUserTaskRequest);
    List<UserTaskResponse> selectAllUserTask();
    UserTaskResponse selectUserTaskByID(Long id);
    void updateUserTaskByID(Long id, UpdateUserTaskRequest updateUserTaskRequest);
    void deleteUserTaskByID(Long id);

}
