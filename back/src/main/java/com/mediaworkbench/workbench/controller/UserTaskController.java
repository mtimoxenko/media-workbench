package com.mediaworkbench.workbench.controller;

import com.mediaworkbench.workbench.dto.usertask.CreateUserTaskRequest;
import com.mediaworkbench.workbench.dto.usertask.UpdateUserTaskRequest;
import com.mediaworkbench.workbench.dto.usertask.UserTaskResponse;
import com.mediaworkbench.workbench.service.IUserTaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Users<->Tasks", description = "Endpoints for management of tasks assigned to users")
@RestController
@RequestMapping("/usertasks")
public class UserTaskController {

    private final IUserTaskService userTaskService;

    @Autowired
    public UserTaskController(IUserTaskService userTaskService) {
        this.userTaskService = userTaskService;
    }

    @Operation(summary = "Get All User Tasks", description = "Returns a list of all tasks assigned to users")
    @ApiResponse(responseCode = "200", description = "Successful operation", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = UserTaskResponse.class))
    })
    @GetMapping()
    public ResponseEntity<List<UserTaskResponse>> getAllUserTasks() {
        List<UserTaskResponse> userTasks = userTaskService.selectAllUserTask();
        return new ResponseEntity<>(userTasks, HttpStatus.OK);
    }

    @Operation(summary = "Get User Task by ID", description = "Returns a specific user task by ID")
    @ApiResponse(responseCode = "200", description = "User task found", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = UserTaskResponse.class))
    })
    @ApiResponse(responseCode = "404", description = "User task not found")
    @GetMapping("/{id}")
    public ResponseEntity<UserTaskResponse> getUserTask(
            @Parameter(description = "ID of the user task to be obtained", required = true) @PathVariable Long id) {
        UserTaskResponse userTaskResponse = userTaskService.selectUserTaskByID(id);
        return new ResponseEntity<>(userTaskResponse, HttpStatus.OK);
    }

    @Operation(summary = "Create User Task", description = "Assigns a new task to a user")
    @ApiResponse(responseCode = "200", description = "User task created successfully")
    @PostMapping
    public ResponseEntity<String> createUserTask(
            @Parameter(description = "Details of the user task to be created", required = true) @RequestBody CreateUserTaskRequest createUserTaskRequest) {
        userTaskService.insertUserTask(createUserTaskRequest);
        return ResponseEntity.ok("User task created successfully!");
    }

    @Operation(summary = "Update User Task", description = "Updates an assigned user task")
    @ApiResponse(responseCode = "200", description = "User task updated successfully")
    @PutMapping
    public ResponseEntity<String> updateUserTask(
            @Parameter(description = "Details of the user task to be updated", required = true) @RequestBody UpdateUserTaskRequest updateUserTaskRequest) {
        userTaskService.updateUserTaskByID(updateUserTaskRequest);
        return ResponseEntity.ok("User task updated successfully!");
    }

    @Operation(summary = "Cancel User Task", description = "Cancels an assigned user task")
    @ApiResponse(responseCode = "200", description = "User task canceled successfully")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> cancelUserTask(
            @Parameter(description = "ID of the user task to be canceled", required = true) @PathVariable Long id) {
        userTaskService.cancelUserTaskByID(id);
        return ResponseEntity.ok("User task with id [" + id + "] canceled successfully!");
    }
}
