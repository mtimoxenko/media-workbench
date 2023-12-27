package com.dentalcura.webapp.controller;


import com.dentalcura.webapp.dto.user.*;
import com.dentalcura.webapp.model.User;
import com.dentalcura.webapp.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    IUserService userService;

    @GetMapping()
    public ResponseEntity<List<UserResponse>> getUserAll() {
        return new ResponseEntity<>(userService.selectAllUser(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUser(@PathVariable Long id) {
        UserResponse userResponse = userService.selectUserByID(id);

        return new ResponseEntity<>(userResponse, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<String> createUser(@RequestBody CreateUserRequest createUserRequest) {
        HttpHeaders httpHeaders = new HttpHeaders();

        httpHeaders.add("user_created", "true");  // custom header
        String message = "User created successfully!";

        userService.insertUser(createUserRequest);
        return ResponseEntity.ok()
                .headers(httpHeaders)
                .body(message);
    }


    @PostMapping("/login")
    public ResponseEntity<LoginUserResponse> loginUser(@RequestBody LoginUserRequest loginUserRequest) {
        HttpHeaders httpHeaders = new HttpHeaders();

        httpHeaders.add("user_login", "true");  // Adding a custom header

        LoginUserResponse loginUserResponse = userService.login(loginUserRequest);

        return ResponseEntity.ok()
                .headers(httpHeaders)
                .body(loginUserResponse);
    }


    @PutMapping("/{id}")
    public ResponseEntity<String> updateUser(@PathVariable Long id, @RequestBody UpdateUserRequest updateUserRequest) {
        HttpHeaders httpHeaders = new HttpHeaders();

        httpHeaders.add("user_updated", "true");  // Adding a custom header
        String message = "User updated successfully!";

        userService.updateUserByID(id, updateUserRequest);
        return ResponseEntity.ok()
                .headers(httpHeaders)
                .body(message);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        HttpHeaders httpHeaders = new HttpHeaders();

        httpHeaders.add("user_deleted", "true");  // Adding a custom header
        String message = "User deleted successfully!";

        userService.deleteUserByID(id);
        return ResponseEntity.ok()
                .headers(httpHeaders)
                .body(message);
    }

}
