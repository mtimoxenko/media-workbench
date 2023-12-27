package com.nocme.workbench.service.impl;


import com.dentalcura.webapp.dto.user.*;
import com.nocme.workbench.model.User;
import com.nocme.workbench.repository.IUserRepository;
import com.nocme.workbench.service.IUserService;
import com.nocme.workbench.utils.exceptions.CustomNotFoundException;
import com.nocme.workbench.utils.exceptions.DuplicateEmailException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nocme.workbench.dto.user.*;
import lombok.Getter;
import lombok.Setter;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


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
        if (!userRepository.existsById(id))
            throw new CustomNotFoundException("User id [" + id + "] not found");

        Optional<User> user = userRepository.findById(id);
        UserResponse userResponse = null;

        if(user.isPresent())
            userResponse = mapper.convertValue(user, UserResponse.class);

        return userResponse;
    }

    @Override
    public void updateUserByID(Long id, UpdateUserRequest updateUserRequest) {
        if (!userRepository.existsById(id))
            throw new CustomNotFoundException("User id [" + id + "] not found");
        if (isEmailDuplicated(updateUserRequest.email())) {
            throw new DuplicateEmailException("Email [" + updateUserRequest.email() + "] is already in use.");
        }

        Optional<User> optionalUser = userRepository.findById(id);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            LOGGER.info("Request to update user id [" + id + "]");

            user.setId(id);
            user.setName(updateUserRequest.name());
            user.setSurname(updateUserRequest.surname());
            user.setEmail(updateUserRequest.email());
            user.setPassword(updateUserRequest.password());
            user.setAdmin(updateUserRequest.admin());

            userRepository.save(user);
            LOGGER.info("User [" + user.getName() + " " + user.getSurname() + "] updated");
        }

    }

    @Override
    public void deleteUserByID(Long id) {
        if (!userRepository.existsById(id))
            throw new CustomNotFoundException("User id [" + id + "] not found");

        userRepository.deleteById(id);
        LOGGER.info("User deleted from DB");
    }


    public LoginUserResponse login(LoginUserRequest loginUserRequest){
        List<User> users = userRepository.findAll();
        int token = 0;
        String userName = null;

        for (User user: users) {
            if(user.getEmail().equals(loginUserRequest.email()) && user.getPassword().equals(loginUserRequest.password())){
                token = user.isAdmin() ? 33 : 1;
                userName = user.getName();
                break;
            }
        }

        return new LoginUserResponse(token, userName);
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
