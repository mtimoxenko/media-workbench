package com.dentalcura.webapp.dto.patient;

public record CreatePatientRequest(
        String name,
        String surname,
        Integer niNumber,
        String registrationDate,
        Address address

) { }
