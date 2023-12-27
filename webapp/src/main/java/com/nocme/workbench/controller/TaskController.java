package com.dentalcura.webapp.controller;


import com.dentalcura.webapp.dto.task.CreateTaskRequest;
import com.dentalcura.webapp.dto.task.TaskResponse;
import com.dentalcura.webapp.dto.task.UpdateTaskRequest;
import com.dentalcura.webapp.service.ITaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/task")
public class TaskController {

    @Autowired
    ITaskService taskService;

    @GetMapping()
    public ResponseEntity<List<TaskResponse>> getTaskAll() {
        return new ResponseEntity<>(taskService.selectAllTask(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskResponse> getTask(@PathVariable Long id) {
        TaskResponse taskResponse = taskService.selectTaskByID(id);

        return new ResponseEntity<>(taskResponse, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<String> createTask(@RequestBody CreateTaskRequest createTaskRequest) {
        HttpHeaders httpHeaders = new HttpHeaders();

        httpHeaders.add("task_created", "true");  // Adding a custom header
        String message = "Comment created successfully!";

        taskService.insertTask(createTaskRequest);
        return ResponseEntity.ok()
                .headers(httpHeaders)
                .body(message);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateTask(@PathVariable Long id, @RequestBody UpdateTaskRequest updateTaskRequest) {
        HttpHeaders httpHeaders = new HttpHeaders();

        httpHeaders.add("task_updated", "true");  // Adding a custom header
        String message = "Comment updated successfully!";

        taskService.updateTaskByID(id, updateTaskRequest);
        return ResponseEntity.ok()
                .headers(httpHeaders)
                .body(message);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTask(@PathVariable Long id) {
        HttpHeaders httpHeaders = new HttpHeaders();

        httpHeaders.add("task_deleted", "true");  // Adding a custom header
        String message = "Comment deleted successfully!";

        taskService.deleteTaskByID(id);
        return ResponseEntity.ok()
                .headers(httpHeaders)
                .body(message);
    }

}
