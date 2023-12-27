package com.nocme.workbench.service;

import com.dentalcura.webapp.dto.user.*;
import com.nocme.workbench.dto.user.*;

import java.util.List;



public interface IUserService {

    void insertUser(CreateUserRequest createUserRequest);
    List<UserResponse> selectAllUser();
    UserResponse selectUserByID(Long id);
    void updateUserByID(Long id, UpdateUserRequest updateUserRequest);
    void deleteUserByID(Long id);

    LoginUserResponse login(LoginUserRequest loginUserRequest);
}
