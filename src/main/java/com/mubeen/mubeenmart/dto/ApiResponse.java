package com.mubeen.mubeenmart.dto;

/**
 * Standard fixed API response envelope conforming to Section 13 specification:
 * { "success": true, "data": { ... }, "error": null }
 * { "success": false, "data": null, "error": { "code": "...", "message": "..." } }
 *
 * Implements Builder Pattern as required by Section 12.
 *
 * @param <T> data payload type
 */
public class ApiResponse<T> {

    private final boolean success;
    private final T data;
    private final ApiError error;

    private ApiResponse(Builder<T> builder) {
        this.success = builder.success;
        this.data = builder.data;
        this.error = builder.error;
    }

    public boolean isSuccess() {
        return success;
    }

    public T getData() {
        return data;
    }

    public ApiError getError() {
        return error;
    }

    public static <T> ApiResponse<T> success(T data) {
        return new Builder<T>()
                .success(true)
                .data(data)
                .build();
    }

    public static <T> ApiResponse<T> error(String code, String message) {
        return new Builder<T>()
                .success(false)
                .error(new ApiError(code, message))
                .build();
    }

    public static <T> ApiResponse<T> error(String code, String message, Object details) {
        return new Builder<T>()
                .success(false)
                .error(new ApiError(code, message, details))
                .build();
    }

    public static <T> Builder<T> builder() {
        return new Builder<>();
    }

    public static class Builder<T> {
        private boolean success;
        private T data;
        private ApiError error;

        public Builder<T> success(boolean success) {
            this.success = success;
            return this;
        }

        public Builder<T> data(T data) {
            this.data = data;
            return this;
        }

        public Builder<T> error(ApiError error) {
            this.error = error;
            return this;
        }

        public ApiResponse<T> build() {
            return new ApiResponse<>(this);
        }
    }
}
