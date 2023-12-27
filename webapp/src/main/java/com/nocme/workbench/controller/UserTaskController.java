package com.dentalcura.webapp.controller;


import com.dentalcura.webapp.dto.usertask.CreateUserTaskRequest;
import com.dentalcura.webapp.dto.usertask.UserTaskResponse;
import com.dentalcura.webapp.dto.usertask.UpdateUserTaskRequest;
import com.dentalcura.webapp.service.IUserTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/userTask")
public class UserTaskController {

    @Autowired
    IUserTaskService userTaskService;

    @GetMapping()
    public ResponseEntity<List<UserTaskResponse>> getUserTaskAll() {
        return new ResponseEntity<>(userTaskService.selectAllUserTask(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserTaskResponse> getUserTask(@PathVariable Long id) {
        UserTaskResponse userTaskResponse = userTaskService.selectUserTaskByID(id);

        return new ResponseEntity<>(userTaskResponse, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<String> createUserTask(@RequestBody CreateUserTaskRequest createUserTaskRequest) {
        HttpHeaders httpHeaders = new HttpHeaders();

        httpHeaders.add("userTask_created", "true");  // Adding a custom header
        String message = "UserTask created successfully!";

        userTaskService.insertUserTask(createUserTaskRequest);
        return ResponseEntity.ok()
                .headers(httpHeaders)
                .body(message);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateUserTask(@PathVariable Long id, @RequestBody UpdateUserTaskRequest updateUserTaskRequest) {
        HttpHeaders httpHeaders = new HttpHeaders();

        httpHeaders.add("userTask_updated", "true");  // Adding a custom header
        String message = "UserTask updated successfully!";

        userTaskService.updateUserTaskByID(id, updateUserTaskRequest);
        return ResponseEntity.ok()
                .headers(httpHeaders)
                .body(message);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUserTask(@PathVariable Long id) {
        HttpHeaders httpHeaders = new HttpHeaders();

        httpHeaders.add("userTask_deleted", "true");  // Adding a custom header
        String message = "UserTask deleted successfully!";

        userTaskService.deleteUserTaskByID(id);
        return ResponseEntity.ok()
                .headers(httpHeaders)
                .body(message);
    }

}
