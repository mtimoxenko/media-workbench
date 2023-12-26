package com.dentalcura.webapp.controller;


import com.dentalcura.webapp.dto.appointment.CreateAppointmentRequest;
import com.dentalcura.webapp.dto.appointment.AppointmentResponse;
import com.dentalcura.webapp.dto.appointment.UpdateAppointmentRequest;
import com.dentalcura.webapp.service.IAppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/appointment")
public class AppointmentController {

    @Autowired
    IAppointmentService appointmentService;

    @GetMapping()
    public ResponseEntity<List<AppointmentResponse>> getAppointmentAll() {
        return new ResponseEntity<>(appointmentService.selectAllAppointment(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AppointmentResponse> getAppointment(@PathVariable Long id) {
        AppointmentResponse appointmentResponse = appointmentService.selectAppointmentByID(id);

        return new ResponseEntity<>(appointmentResponse, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<String> createAppointment(@RequestBody CreateAppointmentRequest createAppointmentRequest) {
        HttpHeaders httpHeaders = new HttpHeaders();

        httpHeaders.add("appointment_created", "true");  // Adding a custom header
        String message = "UserTask created successfully!";

        appointmentService.insertAppointment(createAppointmentRequest);
        return ResponseEntity.ok()
                .headers(httpHeaders)
                .body(message);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateAppointment(@PathVariable Long id, @RequestBody UpdateAppointmentRequest updateAppointmentRequest) {
        HttpHeaders httpHeaders = new HttpHeaders();

        httpHeaders.add("appointment_updated", "true");  // Adding a custom header
        String message = "UserTask updated successfully!";

        appointmentService.updateAppointmentByID(id, updateAppointmentRequest);
        return ResponseEntity.ok()
                .headers(httpHeaders)
                .body(message);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAppointment(@PathVariable Long id) {
        HttpHeaders httpHeaders = new HttpHeaders();

        httpHeaders.add("appointment_deleted", "true");  // Adding a custom header
        String message = "UserTask deleted successfully!";

        appointmentService.deleteAppointmentByID(id);
        return ResponseEntity.ok()
                .headers(httpHeaders)
                .body(message);
    }

}
