package com.mubeen.mubeenmart.exception;

/**
 * Exception thrown when user does not have permission for an operation. Corresponds to HTTP 403.
 */
public class AuthorizationException extends AppException {

    public AuthorizationException(String message) {
        super(message, 403);
    }
}
