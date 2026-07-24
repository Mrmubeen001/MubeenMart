package com.mubeen.mubeenmart.util;

import com.mubeen.mubeenmart.exception.ValidationException;

import java.math.BigDecimal;
import java.util.regex.Pattern;

/**
 * Utility for input validation and sanitization across service and controller layers.
 */
public final class ValidationUtil {

    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,63}$"
    );

    private ValidationUtil() {
        // Prevent instantiation
    }

    /**
     * Validates that an email format is syntactically correct.
     *
     * @param email the email to test
     * @return true if valid
     */
    public static boolean isValidEmail(String email) {
        if (email == null) {
            return false;
        }
        return EMAIL_PATTERN.matcher(email.trim()).matches();
    }

    /**
     * Validates that a password satisfies minimum complexity requirements.
     *
     * @param password the password to validate
     * @return true if password is at least 6 characters
     */
    public static boolean isValidPassword(String password) {
        return password != null && password.length() >= 6;
    }

    /**
     * Requires a string to be non-null and not blank.
     *
     * @param value     the string value
     * @param fieldName the field name for error reporting
     * @return trimmed string
     * @throws ValidationException if string is null or blank
     */
    public static String requireNonBlank(String value, String fieldName) throws ValidationException {
        if (value == null || value.trim().isEmpty()) {
            throw new ValidationException(fieldName + " is required and cannot be blank");
        }
        return value.trim();
    }

    /**
     * Validates positive decimal amount (e.g., product prices, order totals).
     *
     * @param amount the amount to validate
     * @param fieldName the field name for error reporting
     * @throws ValidationException if amount is null or <= 0
     */
    public static void requirePositive(BigDecimal amount, String fieldName) throws ValidationException {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValidationException(fieldName + " must be greater than zero");
        }
    }

    /**
     * Validates non-negative integer (e.g., stock quantity).
     *
     * @param value the integer value
     * @param fieldName the field name for error reporting
     * @throws ValidationException if value is negative
     */
    public static void requireNonNegative(int value, String fieldName) throws ValidationException {
        if (value < 0) {
            throw new ValidationException(fieldName + " cannot be negative");
        }
    }

    /**
     * Escapes standard HTML special characters to prevent cross-site scripting (XSS).
     *
     * @param input the raw input string
     * @return sanitized string
     */
    public static String escapeHtml(String input) {
        if (input == null) {
            return null;
        }
        return input.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#x27;");
    }
}
