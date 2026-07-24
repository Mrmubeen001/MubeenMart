package com.mubeen.mubeenmart.exception;

/**
 * Base checked application exception.
 */
public class AppException extends Exception {

    private final int statusCode;

    public AppException(String message) {
        this(message, 500);
    }

    public AppException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }

    public AppException(String message, Throwable cause) {
        this(message, 500, cause);
    }

    public AppException(String message, int statusCode, Throwable cause) {
        super(message, cause);
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
