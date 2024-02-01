package com.mediaworkbench.workbench.controller;

import com.mediaworkbench.workbench.dto.schedule.CreateScheduleRequest;
import com.mediaworkbench.workbench.dto.schedule.UpdateScheduleRequest;
import com.mediaworkbench.workbench.dto.schedule.ScheduleResponse;
import com.mediaworkbench.workbench.service.IScheduleService;
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

@Tag(name = "Schedule", description = "Endpoints for schedule management")
@RestController
@RequestMapping("/schedule")
public class ScheduleController {

    @Autowired
    private IScheduleService scheduleService;

    @Operation(summary = "Get All Schedules", description = "Returns a list of all schedules")
    @ApiResponse(responseCode = "200", description = "Successful operation",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ScheduleResponse.class)))
    @GetMapping()
    public ResponseEntity<List<ScheduleResponse>> getAllSchedules() {
        List<ScheduleResponse> schedules = scheduleService.getAllSchedules();
        return new ResponseEntity<>(schedules, HttpStatus.OK);
    }

    @Operation(summary = "Get a Schedule by ID", description = "Returns a single schedule by ID")
    @ApiResponse(responseCode = "200", description = "Schedule found",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ScheduleResponse.class)))
    @ApiResponse(responseCode = "404", description = "Schedule not found")
    @GetMapping("/{id}")
    public ResponseEntity<ScheduleResponse> getSchedule(
            @Parameter(description = "ID of the schedule to be obtained", required = true) @PathVariable Long id) {
        ScheduleResponse scheduleResponse = scheduleService.getScheduleById(id);
        return new ResponseEntity<>(scheduleResponse, HttpStatus.OK);
    }

    @Operation(summary = "Create a Schedule", description = "Creates a new schedule")
    @ApiResponse(responseCode = "200", description = "Schedule created successfully")
    @PostMapping
    public ResponseEntity<String> createSchedule(
            @Parameter(description = "Schedule object to be created", required = true) @RequestBody CreateScheduleRequest createScheduleRequest) {
        scheduleService.createSchedule(createScheduleRequest);
        return ResponseEntity.ok("Schedule created successfully!");
    }

    @Operation(summary = "Update a Schedule", description = "Updates an existing schedule")
    @ApiResponse(responseCode = "200", description = "Schedule updated successfully")
    @PutMapping
    public ResponseEntity<String> updateSchedule(
            @Parameter(description = "Schedule object to be updated", required = true) @RequestBody UpdateScheduleRequest updateScheduleRequest) {
        scheduleService.updateSchedule(updateScheduleRequest);
        return ResponseEntity.ok("Schedule updated successfully!");
    }

    @Operation(summary = "Delete a Schedule", description = "Deletes a schedule by ID")
    @ApiResponse(responseCode = "200", description = "Schedule deleted successfully")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteSchedule(
            @Parameter(description = "ID of the schedule to be deleted", required = true) @PathVariable Long id) {
        scheduleService.deleteSchedule(id);
        return ResponseEntity.ok("Schedule deleted successfully!");
    }
}
