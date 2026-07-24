package com.mubeen.mubeenmart.util;

import com.mubeen.mubeenmart.exception.ValidationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ValidationUtil Tests")
class ValidationUtilTest {

    @Test
    @DisplayName("Should validate correct email addresses")
    void testEmailValidation() {
        assertTrue(ValidationUtil.isValidEmail("mubeen@example.com"));
        assertTrue(ValidationUtil.isValidEmail("admin@mubeenmart.com"));
        assertFalse(ValidationUtil.isValidEmail("invalid-email"));
        assertFalse(ValidationUtil.isValidEmail("@missingusername.com"));
        assertFalse(ValidationUtil.isValidEmail(null));
    }

    @Test
    @DisplayName("Should validate password minimum length")
    void testPasswordValidation() {
        assertTrue(ValidationUtil.isValidPassword("secret123"));
        assertTrue(ValidationUtil.isValidPassword("123456"));
        assertFalse(ValidationUtil.isValidPassword("12345"));
        assertFalse(ValidationUtil.isValidPassword(null));
    }

    @Test
    @DisplayName("Should require non-blank strings")
    void testRequireNonBlank() {
        assertDoesNotThrow(() -> ValidationUtil.requireNonBlank("valid", "name"));
        assertThrows(ValidationException.class, () -> ValidationUtil.requireNonBlank("   ", "name"));
        assertThrows(ValidationException.class, () -> ValidationUtil.requireNonBlank(null, "name"));
    }

    @Test
    @DisplayName("Should require positive monetary amounts")
    void testRequirePositive() {
        assertDoesNotThrow(() -> ValidationUtil.requirePositive(new BigDecimal("10.50"), "price"));
        assertThrows(ValidationException.class, () -> ValidationUtil.requirePositive(BigDecimal.ZERO, "price"));
        assertThrows(ValidationException.class, () -> ValidationUtil.requirePositive(new BigDecimal("-5.00"), "price"));
    }

    @Test
    @DisplayName("Should escape HTML characters properly")
    void testEscapeHtml() {
        String input = "<script>alert('xss');</script>&\"";
        String escaped = ValidationUtil.escapeHtml(input);
        assertFalse(escaped.contains("<script>"));
        assertTrue(escaped.contains("&lt;script&gt;"));
    }
}
