package com.mubeen.mubeenmart.exception;

/**
 * Exception thrown when user authentication fails. Corresponds to HTTP 401.
 */
public class AuthenticationException extends AppException {

    public AuthenticationException(String message) {
        super(message, 401);
    }
}
