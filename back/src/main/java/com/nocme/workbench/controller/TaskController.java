package com.nocme.workbench.controller;

import com.nocme.workbench.dto.task.*;
import com.nocme.workbench.service.ITaskService;
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
    public ResponseEntity<List<TaskResponse>> getAllTasks() {
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
        httpHeaders.add("task_created", "true");  // custom header
        String message = "Task created successfully!";

        taskService.insertTask(createTaskRequest);
        return ResponseEntity.ok()
                .headers(httpHeaders)
                .body(message);
    }

    @PutMapping
    public ResponseEntity<String> updateTask(@RequestBody UpdateTaskRequest updateTaskRequest) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("task_updated", "true");  // Adding a custom header
        String message = "Task updated successfully!";

        taskService.updateTaskByID(updateTaskRequest);
        return ResponseEntity.ok()
                .headers(httpHeaders)
                .body(message);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTask(@PathVariable Long id) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("task_deleted", "true");  // Adding a custom header
        String message = "Task deleted successfully!";

        taskService.deleteTaskByID(id);
        return ResponseEntity.ok()
                .headers(httpHeaders)
                .body(message);
    }
}
