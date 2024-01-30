package com.mediaworkbench.workbench.service;

import com.mediaworkbench.workbench.dto.user.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IUserService {

    LoginUserResponse insertUser(CreateUserRequest createUserRequest);
    List<UserResponse> selectAllUser();
    UserResponse selectUserByID(Long id);
    void updateUserByID(UpdateUserRequest updateUserRequest);
    void deactivateUserByID(Long id);

    LoginUserResponse login(LoginUserRequest loginUserRequest);

    List<ScheduleResponse> processUserScheduleFile(MultipartFile file);
}
