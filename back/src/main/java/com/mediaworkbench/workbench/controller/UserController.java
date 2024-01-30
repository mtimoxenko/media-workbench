package com.mediaworkbench.workbench.controller;

import com.mediaworkbench.workbench.dto.user.*;
import com.mediaworkbench.workbench.service.IUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Users", description = "Endpoints for user management")
@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private IUserService userService;

    @Operation(summary = "Get All Users", description = "Returns a list of all users")
    @ApiResponse(responseCode = "200", description = "Successful operation",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = UserResponse.class)))
    @GetMapping()
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        return new ResponseEntity<>(userService.selectAllUser(), HttpStatus.OK);
    }

    @Operation(summary = "Get a User by ID", description = "Returns a single user by ID")
    @ApiResponse(responseCode = "200", description = "User found",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = UserResponse.class)))
    @ApiResponse(responseCode = "404", description = "User not found")
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUser(
            @Parameter(description = "ID of the user to be obtained", required = true) @PathVariable Long id) {
        UserResponse userResponse = userService.selectUserByID(id);
        return new ResponseEntity<>(userResponse, HttpStatus.OK);
    }

    @Operation(summary = "Create a User", description = "Creates a new user and returns login information")
    @ApiResponse(responseCode = "201", description = "User created and logged in successfully",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = LoginUserResponse.class)))
    @PostMapping
    public ResponseEntity<LoginUserResponse> createUser(
            @Parameter(description = "User object to be created", required = true) @RequestBody CreateUserRequest createUserRequest) {
        LoginUserResponse loginUserResponse = userService.insertUser(createUserRequest);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("user_created", "true");
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .headers(httpHeaders)
                .body(loginUserResponse);
    }


    @Operation(summary = "User Login", description = "Authenticates a user and returns login information")
    @ApiResponse(responseCode = "200", description = "Login successful",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = LoginUserResponse.class)))
    @PostMapping("/login")
    public ResponseEntity<LoginUserResponse> loginUser(
            @Parameter(description = "User login credentials", required = true) @RequestBody LoginUserRequest loginUserRequest) {
        LoginUserResponse loginUserResponse = userService.login(loginUserRequest);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("user_login", "true");
        return ResponseEntity.ok()
                .headers(httpHeaders)
                .body(loginUserResponse);
    }

    @Operation(summary = "Update a User", description = "Updates an existing user")
    @ApiResponse(responseCode = "200", description = "User updated successfully")
    @PutMapping
    public ResponseEntity<String> updateUser(
            @Parameter(description = "User object to be updated", required = true) @RequestBody UpdateUserRequest updateUserRequest) {
        userService.updateUserByID(updateUserRequest);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("user_updated", "true");
        return ResponseEntity.ok()
                .headers(httpHeaders)
                .body("User updated successfully!");
    }

    @Operation(summary = "Deactivate a User", description = "Deactivates a user by ID")
    @ApiResponse(responseCode = "200", description = "User deactivated successfully")
    @PutMapping("/deactivate/{id}")
    public ResponseEntity<String> deactivateUser(
            @Parameter(description = "ID of the user to be deactivated", required = true) @PathVariable Long id) {
        userService.deactivateUserByID(id);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("user_deactivated", "true");
        return ResponseEntity.ok()
                .headers(httpHeaders)
                .body("User deactivated successfully!");
    }
}
