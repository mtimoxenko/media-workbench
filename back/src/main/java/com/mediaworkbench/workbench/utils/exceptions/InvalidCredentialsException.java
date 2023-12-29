package com.mediaworkbench.workbench.utils.exceptions;

public class InvalidCredentialsException extends RuntimeException {

    public InvalidCredentialsException(String message) {
        super(message);
    }

    // Optionally, you can add more constructors for different use cases
    // For example, an exception with a cause:
    public InvalidCredentialsException(String message, Throwable cause) {
        super(message, cause);
    }
}
