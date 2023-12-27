package com.dentalcura.webapp.service.impl;

import com.dentalcura.webapp.dto.user.CreateUserRequest;
import com.dentalcura.webapp.dto.user.LoginUserRequest;
import com.dentalcura.webapp.dto.user.LoginUserResponse;
import com.dentalcura.webapp.dto.user.UpdateUserRequest;
import com.dentalcura.webapp.model.User;
import com.dentalcura.webapp.repository.IUserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceTest {

    @Autowired
    IUserRepository userRepository;
    @Autowired
    ObjectMapper mapper;

    UserServiceTest() {
    }


    @Test
    void insertUser() {

        CreateUserRequest createUserRequest = new CreateUserRequest(
              "testName",
              "testSurname",
              "test@email.com",
              "testPassword",
              true
        );

        User user = mapper.convertValue(createUserRequest, User.class);
        userRepository.save(user);

        List<User> users = userRepository.findAll();

        boolean found = false;

        for (User findUser: users) {
            if (user.equals(findUser)) {
                found = true;
                break;
            }
        }

        assertTrue(found);
    }

    @Test
    void updateUserByID() {

        CreateUserRequest createUserRequest = new CreateUserRequest(
                "testName",
                "testSurname",
                "test@email.com",
                "testPassword",
                true
        );

        User user = mapper.convertValue(createUserRequest, User.class);
        userRepository.save(user);

        UpdateUserRequest updateUserRequest = new UpdateUserRequest(
                "testNameUpdate",
                "testSurnameUpdate",
                "testUpdate@email.com",
                "testPasswordUpdate",
                false
        );

        User userUpdate = mapper.convertValue(updateUserRequest, User.class);
        userUpdate.setId(1L);
        userRepository.save(userUpdate);

        Optional<User> optionalUser = userRepository.findById(1L);
        User newUser = null;

        if(optionalUser.isPresent())
            newUser = optionalUser.get();


        assertEquals(userUpdate, newUser);
    }

    @Test
    void deleteUserByID() {

        CreateUserRequest createUserRequestToDelete = new CreateUserRequest(
                "nameDelete",
                "surnameDelete",
                "email@delete.com",
                "passDelete",
                false
        );

        User userToDelete = mapper.convertValue(createUserRequestToDelete, User.class);
        userRepository.save(userToDelete);

        userRepository.deleteById(4L);

        List<User> users = userRepository.findAll();

        boolean found = false;

        for (User findUser: users) {
            if (findUser.equals(userToDelete)) {
                found = true;
                break;
            }
        }

        assertFalse(found);
    }

    @Test
    void login() {

        CreateUserRequest createUserRequest = new CreateUserRequest(
                "testName",
                "testSurname",
                "test@email.com",
                "testPassword",
                true
        );

        User user = mapper.convertValue(createUserRequest, User.class);
        userRepository.save(user);

        LoginUserRequest loginUserRequest = new LoginUserRequest(
                "test@email.com",
                "testPassword"
        );

        List<User> users = userRepository.findAll();
        int token = 0;
        String userName = null;

        for (User userFound: users) {
            if(userFound.getEmail().equals(loginUserRequest.email()) && userFound.getPassword().equals(loginUserRequest.password())){
                token = user.isAdmin() ? 33 : 1;
                userName = user.getName();
                break;
            }
        }

        LoginUserResponse loginUserResponse = new LoginUserResponse(
                token,
                userName
        );

        assertEquals(33, loginUserResponse.token());
        assertEquals("testName", loginUserResponse.userName());
    }
}