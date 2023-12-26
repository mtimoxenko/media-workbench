package com.dentalcura.webapp.dto.patient;

public record UpdatePatientRequest(
        String name,
        String surname,
        Address address

) { }