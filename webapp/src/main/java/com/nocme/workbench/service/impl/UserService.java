package com.nocme.workbench.service.impl;

import com.nocme.workbench.model.User;
import com.nocme.workbench.repository.IUserRepository;
import com.nocme.workbench.service.IUserService;
import com.nocme.workbench.utils.exceptions.CustomNotFoundException;
import com.nocme.workbench.utils.exceptions.DuplicateEmailException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nocme.workbench.dto.user.*;
import com.nocme.workbench.utils.exceptions.InvalidCredentialsException;
import lombok.Getter;
import lombok.Setter;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
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

        if (isEmailDuplicated(createUserRequest.email())) {
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
        if (isEmailDuplicated(updateUserRequest.email())) {
            throw new DuplicateEmailException("Email [" + updateUserRequest.email() + "] is already in use.");
        }

        User user = mapper.convertValue(updateUserRequest, User.class);
        userRepository.save(user);
        LOGGER.info("User id [" + updateUserRequest.id() + "] updated!");
//        Optional<User> optionalUser = userRepository.findById(updateUserRequest.id());
//        if (optionalUser.isPresent()) {
//            User user = optionalUser.get();
//            LOGGER.info("Request to update user id [" + updateUserRequest.id() + "]");
//
//            user.setId(updateUserRequest.id());
//            user.setName(updateUserRequest.name());
//            user.setSurname(updateUserRequest.surname());
//            user.setEmail(updateUserRequest.email());
//            user.setPassword(updateUserRequest.password());
//            user.setAdmin(updateUserRequest.admin());
//
//            userRepository.save(user);
//            LOGGER.info("User [" + user.getName() + " " + user.getSurname() + "] updated");
//        }
    }

    @Override
    public void deleteUserByID(Long id) {
        if (!userRepository.existsById(id))
            throw new CustomNotFoundException("User id [" + id + "] not found");

        userRepository.deleteById(id);
        LOGGER.info("User id [" + id + "] deleted from DB");
    }


    public LoginUserResponse login(LoginUserRequest loginUserRequest) {
        User user = userRepository.findByEmail(loginUserRequest.email())
                .orElseThrow(() -> new CustomNotFoundException("User not found"));

        if (!user.getPassword().equals(loginUserRequest.password())) {
            throw new InvalidCredentialsException("Invalid password");
        }

        int token = user.getIsAdmin() ? 33 : 1;
        return new LoginUserResponse(token, user.getName());
    }

    
    private boolean isEmailDuplicated(String email){
        List<User> users = userRepository.findAll();
        boolean isDuplicated = false;

        for(User user: users){
            if (user.getEmail().equals(email)) {
                isDuplicated = true;
                break;
            }
        }

        return isDuplicated;
    }
}
