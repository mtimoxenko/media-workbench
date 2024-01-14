package com.mediaworkbench.workbench.service.impl;

import com.mediaworkbench.workbench.dto.user.*;
import com.mediaworkbench.workbench.model.UserRolStatus;
import com.mediaworkbench.workbench.model.UserShiftStatus;
import com.mediaworkbench.workbench.repository.IUserRepository;
import com.mediaworkbench.workbench.dto.usertask.UserTaskResponse;
import com.mediaworkbench.workbench.model.User;
import com.mediaworkbench.workbench.service.IUserService;
import com.mediaworkbench.workbench.utils.exceptions.CustomDatabaseException;
import com.mediaworkbench.workbench.utils.exceptions.CustomNotFoundException;
import com.mediaworkbench.workbench.utils.exceptions.DuplicateEmailException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mediaworkbench.workbench.utils.exceptions.InvalidCredentialsException;
import jakarta.transaction.Transactional;
import lombok.Getter;
import lombok.Setter;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Getter @Setter
@Service
public class UserService implements IUserService {

    private final static Logger LOGGER = Logger.getLogger(UserService.class);

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private ObjectMapper mapper;

    @Override
    public LoginUserResponse insertUser(CreateUserRequest createUserRequest) {

        if (isEmailDuplicated(createUserRequest.email(), null)) {
            throw new DuplicateEmailException("Email [" + createUserRequest.email() + "] is already in use.");
        }

        User user = mapper.convertValue(createUserRequest, User.class);
        userRepository.save(user);
        LOGGER.info("New user was registered [" + user.getName() + " " + user.getSurname() + "]");

        // Create a LoginUserResponse for the new user
        int token = user.getIsAdmin() ? 33 : 1; // Token generation logic, can be modified as needed
        return new LoginUserResponse(token, user.getName(), user.getId(), user.getShift(), user.getRol());
    }

    @Override
    @Transactional
    public List<UserResponse> selectAllUser() {
        List<User> users = userRepository.findAll();

        return users.stream().map(user -> {
            List<UserTaskResponse> userTaskResponses = user.getAssignedTasks().stream().map(userTask -> new UserTaskResponse(
                    userTask.getId(),
                    userTask.getAssignmentDate(),
                    userTask.getUser().getName(),       // User's name to whom the task is assigned
                    userTask.getUser().getSurname(),    // User's surname to whom the task is assigned
                    userTask.getTask().getId(),         // Task's ID
                    userTask.getTask().getName(),       // Task's name
                    userTask.getUserTaskStatus().toString(), // UserTask status
                    userTask.getAssigner().getName(),   // Assigner's name
                    userTask.getAssigner().getSurname() // Assigner's surname
            )).collect(Collectors.toList());

            return new UserResponse(
                    user.getId(),
                    user.getName(),
                    user.getSurname(),
                    user.getEmail(),
                    user.getShift(),
                    user.getRol(),
                    user.getIsAdmin(),
                    userTaskResponses // List of UserTaskResponses
            );
        }).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public UserResponse selectUserByID(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new CustomNotFoundException("User id [" + id + "] not found"));

        List<UserTaskResponse> userTaskResponses = user.getAssignedTasks().stream().map(userTask -> new UserTaskResponse(
                userTask.getId(),
                userTask.getAssignmentDate(),
                userTask.getUser().getName(),       // User's name to whom the task is assigned
                userTask.getUser().getSurname(),    // User's surname to whom the task is assigned
                userTask.getTask().getId(),         // Task's ID
                userTask.getTask().getName(),       // Task's name
                userTask.getUserTaskStatus().toString(), // UserTask status
                userTask.getAssigner().getName(),   // Assigner's name
                userTask.getAssigner().getSurname() // Assigner's surname
        )).collect(Collectors.toList());

        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getSurname(),
                user.getEmail(),
                user.getShift(),
                user.getRol(),
                user.getIsAdmin(),
                userTaskResponses // List of UserTaskResponses
        );
    }








    @Override
    public void updateUserByID(UpdateUserRequest updateUserRequest) {
        if (!userRepository.existsById(updateUserRequest.id()))
            throw new CustomNotFoundException("User id [" + updateUserRequest.id() + "] not found.");
        if (isEmailDuplicated(updateUserRequest.email(), updateUserRequest.id())) {
            throw new DuplicateEmailException("Email [" + updateUserRequest.email() + "] is already in use.");
        }

        User existingUser = userRepository.findById(updateUserRequest.id())
                .orElseThrow(() -> new CustomNotFoundException("User id [" + updateUserRequest.id() + "] not found."));

        // Manually update the fields
        existingUser.setName(updateUserRequest.name());
        existingUser.setSurname(updateUserRequest.surname());
        existingUser.setEmail(updateUserRequest.email());
        existingUser.setPassword(updateUserRequest.password());
        existingUser.setShift(updateUserRequest.shift());      // UserShiftStatus
        existingUser.setRol(updateUserRequest.rol());    // UserRolStatus rol
        existingUser.setIsAdmin(updateUserRequest.isAdmin());
        // Do not replace collections, modify them if needed

        userRepository.save(existingUser);
        LOGGER.info("User id [" + updateUserRequest.id() + "] updated!");
    }


    @Override
    @Transactional
    public void deleteUserByID(Long id) {
        LOGGER.info("Attempting to delete user with id: " + id);

        if (!userRepository.existsById(id)) {
            LOGGER.warn("User with id [" + id + "] not found for deletion.");
            throw new CustomNotFoundException("User id [" + id + "] not found");
        }

        try {
            userRepository.deleteById(id);
            LOGGER.info("User with id [" + id + "] successfully deleted from the database.");
        } catch (DataAccessException e) {
            LOGGER.error("Database access error occurred while deleting user with id " + id, e);
            throw new CustomDatabaseException("Failed to delete user due to database access error", e);
        }
    }





    public LoginUserResponse login(LoginUserRequest loginUserRequest) {
        Optional<User> userOptional = userRepository.findByEmail(loginUserRequest.email());

        if (userOptional.isEmpty() || !userOptional.get().getPassword().equals(loginUserRequest.password())) {
            LOGGER.warn("Invalid login attempt with email: " + loginUserRequest.email());
            throw new InvalidCredentialsException("Invalid email or password");
        }

        User user = userOptional.get();
        LOGGER.info("User [" + user.getName() + " " + user.getSurname() + "] logged in successfully.");

        int token = user.getIsAdmin() ? 33 : 1;
        return new LoginUserResponse(token, user.getName(), user.getId(), user.getShift(), user.getRol());
    }



    private boolean isEmailDuplicated(String email, Long userId) {
        if (userId == null) {
            // For new user insertion, check if any user with the given email already exists
            return userRepository.findByEmail(email).isPresent();
        } else {
            // For existing user update, check if any other user with the given email exists
            return userRepository.findByEmailAndIdNot(email, userId).isPresent();
        }
    }


}
