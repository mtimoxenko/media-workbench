package com.mediaworkbench.workbench.service.impl;

import com.mediaworkbench.workbench.dto.user.*;
import com.mediaworkbench.workbench.repository.IUserRepository;
import com.mediaworkbench.workbench.dto.usertask.UserTaskResponse;
import com.mediaworkbench.workbench.model.User;
import com.mediaworkbench.workbench.service.IUserService;
import com.mediaworkbench.workbench.utils.exceptions.CustomNotFoundException;
import com.mediaworkbench.workbench.utils.exceptions.DuplicateEmailException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mediaworkbench.workbench.utils.exceptions.InvalidCredentialsException;
import jakarta.transaction.Transactional;
import lombok.Getter;
import lombok.Setter;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
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
        int token = user.getIsActive() ? 1 : 33; // Token generation logic, can be modified as needed
        return new LoginUserResponse(token, user.getName(), user.getId(), user.getShift(), user.getRole());
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
                    user.getRole(),
                    user.getIsActive(),
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
                user.getRole(),
                user.getIsActive(),
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
        existingUser.setShift(updateUserRequest.shift());      // ShiftStatus
        existingUser.setRole(updateUserRequest.role());    // UserRoleStatus role
        existingUser.setIsActive(updateUserRequest.isActive());
        // Do not replace collections, modify them if needed

        userRepository.save(existingUser);
        LOGGER.info("User id [" + updateUserRequest.id() + "] updated!");
    }


    @Override
    @Transactional
    public void deactivateUserByID(Long id) {
        LOGGER.info("Attempting to deactivate user with id: " + id);

        User user = userRepository.findById(id)
                .orElseThrow(() -> new CustomNotFoundException("User id [" + id + "] not found"));

        user.setIsActive(false);
        userRepository.save(user);

        LOGGER.info("User with id [" + id + "] successfully deactivated.");
    }


    public LoginUserResponse login(LoginUserRequest loginUserRequest) {
        Optional<User> userOptional = userRepository.findByEmail(loginUserRequest.email());

        if (userOptional.isEmpty() || !userOptional.get().getPassword().equals(loginUserRequest.password())) {
            LOGGER.warn("Invalid login attempt with email: " + loginUserRequest.email());
            throw new InvalidCredentialsException("Invalid email or password");
        }

        User user = userOptional.get();
        LOGGER.info("User [" + user.getName() + " " + user.getSurname() + "] logged in successfully.");

        int token = user.getIsActive() ? 1 : 0;
        return new LoginUserResponse(token, user.getName(), user.getId(), user.getShift(), user.getRole());
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


    public List<ScheduleResponse> processUserScheduleFile(MultipartFile file) {
        List<ScheduleResponse> namesList = new ArrayList<>();

        try (InputStream inputStream = file.getInputStream();
             Workbook workbook = new XSSFWorkbook(inputStream)) {

            Sheet sheet = workbook.getSheetAt(0);
            int nameColumnIndex = 1; // Assuming the names are in the second column

            for (Row row : sheet) {
                Cell nameCell = row.getCell(nameColumnIndex);
                if (nameCell != null && nameCell.getCellType() == CellType.STRING) {
                    String name = nameCell.getStringCellValue();
                    // Add extra checks if needed, e.g., to skip headers
                    if (name != null && !name.trim().isEmpty() && !name.equalsIgnoreCase("Apellido y Nombre")) {
                        namesList.add(new ScheduleResponse(name.trim()));
                    }
                }
            }

        } catch (Exception e) {
            LOGGER.error("Error processing user schedule file", e);
            throw new RuntimeException("Error processing user schedule file: " + e.getMessage());
        }

        return namesList;
    }


}
