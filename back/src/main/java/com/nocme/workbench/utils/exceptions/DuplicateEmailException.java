package com.nocme.workbench.utils.exceptions;

public class DuplicateEmailException extends RuntimeException{

    public DuplicateEmailException(String message) {
        super(message);
    }
}
