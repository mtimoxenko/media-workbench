package com.mediaworkbench.workbench.controller;

import com.mediaworkbench.workbench.dto.usertask.CreateUserTaskRequest;
import com.mediaworkbench.workbench.dto.usertask.UpdateUserTaskRequest;
import com.mediaworkbench.workbench.dto.usertask.UserTaskResponse;
import com.mediaworkbench.workbench.service.IUserTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usertasks")
public class UserTaskController {

    private final IUserTaskService userTaskService;

    @Autowired
    public UserTaskController(IUserTaskService userTaskService) {
        this.userTaskService = userTaskService;
    }

    @GetMapping()
    public ResponseEntity<List<UserTaskResponse>> getAllUserTasks() {
        List<UserTaskResponse> userTasks = userTaskService.selectAllUserTask();
        return new ResponseEntity<>(userTasks, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserTaskResponse> getUserTask(@PathVariable Long id) {
        UserTaskResponse userTaskResponse = userTaskService.selectUserTaskByID(id);
        return new ResponseEntity<>(userTaskResponse, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<String> createUserTask(@RequestBody CreateUserTaskRequest createUserTaskRequest) {
        userTaskService.insertUserTask(createUserTaskRequest);
        String message = "User task created successfully!";
        return ResponseEntity.ok().body(message);
    }

    @PutMapping
    public ResponseEntity<String> updateUserTask(@RequestBody UpdateUserTaskRequest updateUserTaskRequest) {
        userTaskService.updateUserTaskByID(updateUserTaskRequest);
        String message = "User task updated successfully!";
        return ResponseEntity.ok().body(message);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUserTask(@PathVariable Long id) {
        userTaskService.deleteUserTaskByID(id);
        String message = "User task deleted successfully!";
        return ResponseEntity.ok().body(message);
    }
}
