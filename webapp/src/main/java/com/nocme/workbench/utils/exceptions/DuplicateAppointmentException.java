package com.dentalcura.webapp.utils.exceptions;

public class DuplicateAppointmentException extends RuntimeException{

    public DuplicateAppointmentException(String message) {
        super(message);
    }
}
