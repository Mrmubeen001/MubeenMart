package com.mubeen.mubeenmart.exception;

/**
 * Exception thrown when a requested resource is not found. Corresponds to HTTP 404.
 */
public class ResourceNotFoundException extends AppException {

    public ResourceNotFoundException(String message) {
        super(message, 404);
    }
}
