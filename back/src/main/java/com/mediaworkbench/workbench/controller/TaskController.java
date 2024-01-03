package com.mediaworkbench.workbench.controller;

import com.mediaworkbench.workbench.dto.task.CreateTaskRequest;
import com.mediaworkbench.workbench.dto.task.TaskResponse;
import com.mediaworkbench.workbench.dto.task.UpdateTaskRequest;
import com.mediaworkbench.workbench.service.ITaskService;
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

@Tag(name = "Tasks", description = "Endpoints for task management")
@RestController
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    private ITaskService taskService;

    @Operation(summary = "Get All Tasks", description = "Returns a list of all tasks")
    @ApiResponse(responseCode = "200", description = "Successful operation",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = TaskResponse.class)))
    @GetMapping()
    public ResponseEntity<List<TaskResponse>> getAllTasks() {
        return new ResponseEntity<>(taskService.selectAllTask(), HttpStatus.OK);
    }

    @Operation(summary = "Get a Task by ID", description = "Returns a single task by ID")
    @ApiResponse(responseCode = "200", description = "Task found",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = TaskResponse.class)))
    @ApiResponse(responseCode = "404", description = "Task not found")
    @GetMapping("/{id}")
    public ResponseEntity<TaskResponse> getTask(
            @Parameter(description = "ID of the task to be obtained", required = true) @PathVariable Long id) {
        TaskResponse taskResponse = taskService.selectTaskByID(id);
        return new ResponseEntity<>(taskResponse, HttpStatus.OK);
    }

    @Operation(summary = "Create a Task", description = "Creates a new task")
    @ApiResponse(responseCode = "200", description = "Task created successfully")
    @PostMapping
    public ResponseEntity<String> createTask(
            @Parameter(description = "Task object to be created", required = true) @RequestBody CreateTaskRequest createTaskRequest) {
        taskService.insertTask(createTaskRequest);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("task_created", "true");
        return ResponseEntity.ok()
                .headers(httpHeaders)
                .body("Task created successfully!");
    }

    @Operation(summary = "Update a Task", description = "Updates an existing task")
    @ApiResponse(responseCode = "200", description = "Task updated successfully")
    @PutMapping
    public ResponseEntity<String> updateTask(
            @Parameter(description = "Task object to be updated", required = true) @RequestBody UpdateTaskRequest updateTaskRequest) {
        taskService.updateTaskByID(updateTaskRequest);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("task_updated", "true");
        return ResponseEntity.ok()
                .headers(httpHeaders)
                .body("Task updated successfully!");
    }

    @Operation(summary = "Cancel a Task", description = "Cancels a task by ID")
    @ApiResponse(responseCode = "200", description = "Task canceled successfully")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> cancelTask(
            @Parameter(description = "ID of the task to be canceled", required = true) @PathVariable Long id) {
        taskService.cancelTaskByID(id);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("task_canceled", "true");
        return ResponseEntity.ok()
                .headers(httpHeaders)
                .body("Task canceled successfully!");
    }
}
