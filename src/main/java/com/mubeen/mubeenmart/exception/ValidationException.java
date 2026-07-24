package com.mubeen.mubeenmart.exception;

/**
 * Exception thrown when input validation fails. Corresponds to HTTP 400.
 */
public class ValidationException extends AppException {

    public ValidationException(String message) {
        super(message, 400);
    }
}
