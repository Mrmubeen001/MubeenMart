package com.mubeen.mubeenmart.exception;

/**
 * Exception thrown when a state conflict occurs (e.g. duplicate email, insufficient stock). Corresponds to HTTP 409.
 */
public class ConflictException extends AppException {

    public ConflictException(String message) {
        super(message, 409);
    }
}
