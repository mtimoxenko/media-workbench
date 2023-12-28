package com.nocme.workbench.service.impl;

import com.nocme.workbench.model.User;
import com.nocme.workbench.repository.IUserRepository;
import com.nocme.workbench.service.IUserService;
import com.nocme.workbench.utils.exceptions.CustomDatabaseException;
import com.nocme.workbench.utils.exceptions.CustomNotFoundException;
import com.nocme.workbench.utils.exceptions.DuplicateEmailException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nocme.workbench.dto.user.*;
import com.nocme.workbench.utils.exceptions.InvalidCredentialsException;
import jakarta.transaction.Transactional;
import lombok.Getter;
import lombok.Setter;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Getter @Setter
@Service
public class UserService implements IUserService {

    private final static Logger LOGGER = Logger.getLogger(UserService.class);

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    ObjectMapper mapper;

    @Override
    public void insertUser(CreateUserRequest createUserRequest) {

        if (isEmailDuplicated(createUserRequest.email(), null)) {
            throw new DuplicateEmailException("Email [" + createUserRequest.email() + "] is already in use.");
        }

        User user = mapper.convertValue(createUserRequest, User.class);
        userRepository.save(user);
        LOGGER.info("New user was registered [" + user.getName() + " " + user.getSurname() + "]");
    }

    @Override
    public List<UserResponse> selectAllUser() {
        List<User> users = userRepository.findAll();
        List<UserResponse> userResponses = new ArrayList<>();

        for(User user: users){
            userResponses.add(mapper.convertValue(user, UserResponse.class));
        }

        return userResponses;
    }

    @Override
    public UserResponse selectUserByID(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new CustomNotFoundException("User id [" + id + "] not found"));

        return mapper.convertValue(user, UserResponse.class);
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
        existingUser.setIsAdmin(updateUserRequest.isAdmin());
        // Do not replace collections, modify them if needed

        userRepository.save(existingUser);
        LOGGER.info("User id [" + updateUserRequest.id() + "] updated!");
    }


    @Override
    @Transactional
    public void deleteUserByID(Long id) {
        LOGGER.info("Attempting to delete user with id: " + id);

        try {
            userRepository.deleteById(id);
            LOGGER.info("User with id [" + id + "] successfully deleted from the database.");
        } catch (DataAccessException e) {
            LOGGER.error("Database access error occurred while deleting user with id " + id, e);
            // Handle or rethrow as appropriate for your application
            throw new CustomDatabaseException("Failed to delete user due to database access error", e);
        } catch (Exception e) {
            LOGGER.error("Unexpected error occurred while deleting user with id " + id, e);
            // Handle or rethrow as appropriate for your application
            throw e;
        }
    }




    public LoginUserResponse login(LoginUserRequest loginUserRequest) {
        User user = userRepository.findByEmail(loginUserRequest.email())
                .orElseThrow(() -> new CustomNotFoundException("User not found"));

        if (!user.getPassword().equals(loginUserRequest.password())) {
            throw new InvalidCredentialsException("Invalid email or password");
        }

        int token = user.getIsAdmin() ? 33 : 1;
        return new LoginUserResponse(token, user.getName());
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
