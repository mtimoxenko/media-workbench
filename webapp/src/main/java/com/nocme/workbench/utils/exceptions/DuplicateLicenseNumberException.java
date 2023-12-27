package com.dentalcura.webapp.utils.exceptions;

public class DuplicateLicenseNumberException extends RuntimeException{
    public DuplicateLicenseNumberException(String message) {
        super(message);
    }
}
