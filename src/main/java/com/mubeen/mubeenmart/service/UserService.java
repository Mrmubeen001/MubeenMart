package com.mubeen.mubeenmart.service;

import com.mubeen.mubeenmart.dto.RegisterRequestDTO;
import com.mubeen.mubeenmart.dto.UserResponseDTO;
import com.mubeen.mubeenmart.exception.AppException;
import com.mubeen.mubeenmart.model.User;

import java.util.List;

/**
 * Service interface for user management and authentication.
 */
public interface UserService {

    /**
     * Registers a new user account with role BUYER or SELLER.
     *
     * @param request registration input DTO
     * @return UserResponseDTO without sensitive fields
     * @throws AppException if validation fails or email already in use
     */
    UserResponseDTO register(RegisterRequestDTO request) throws AppException;

    /**
     * Authenticates a user by email and password.
     *
     * @param email user email
     * @param password candidate password
     * @return authenticated User entity
     * @throws AppException if credentials are invalid
     */
    User login(String email, String password) throws AppException;

    /**
     * Retrieves a user by identifier.
     *
     * @param id user ID
     * @return UserResponseDTO
     * @throws AppException if user not found
     */
    UserResponseDTO getUserById(Long id) throws AppException;

    /**
     * Retrieves all registered users in the marketplace (Admin only).
     *
     * @return list of UserResponseDTO
     * @throws AppException on service failure
     */
    List<UserResponseDTO> getAllUsers() throws AppException;
}
