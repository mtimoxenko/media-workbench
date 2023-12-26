package com.dentalcura.webapp.controller;


import com.dentalcura.webapp.dto.dentist.CreateDentistRequest;
import com.dentalcura.webapp.dto.dentist.DentistResponse;
import com.dentalcura.webapp.dto.dentist.UpdateDentistRequest;
import com.dentalcura.webapp.service.IDentistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/comment")
public class DentistController {

    @Autowired
    IDentistService dentistService;

    @GetMapping()
    public ResponseEntity<List<DentistResponse>> getDentistAll() {
        return new ResponseEntity<>(dentistService.selectAllDentist(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DentistResponse> getDentist(@PathVariable Long id) {
        DentistResponse dentistResponse = dentistService.selectDentistByID(id);

        return new ResponseEntity<>(dentistResponse, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<String> createDentist(@RequestBody CreateDentistRequest createDentistRequest) {
        HttpHeaders httpHeaders = new HttpHeaders();

        httpHeaders.add("dentist_created", "true");  // Adding a custom header
        String message = "Comment created successfully!";

        dentistService.insertDentist(createDentistRequest);
        return ResponseEntity.ok()
                .headers(httpHeaders)
                .body(message);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateDentist(@PathVariable Long id, @RequestBody UpdateDentistRequest updateDentistRequest) {
        HttpHeaders httpHeaders = new HttpHeaders();

        httpHeaders.add("dentist_updated", "true");  // Adding a custom header
        String message = "Comment updated successfully!";

        dentistService.updateDentistByID(id, updateDentistRequest);
        return ResponseEntity.ok()
                .headers(httpHeaders)
                .body(message);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteDentist(@PathVariable Long id) {
        HttpHeaders httpHeaders = new HttpHeaders();

        httpHeaders.add("dentist_deleted", "true");  // Adding a custom header
        String message = "Comment deleted successfully!";

        dentistService.deleteDentistByID(id);
        return ResponseEntity.ok()
                .headers(httpHeaders)
                .body(message);
    }

}
