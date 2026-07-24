package com.mubeen.mubeenmart.dao;

import com.mubeen.mubeenmart.model.User;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * Data Access Object interface for user account persistence.
 */
public interface UserDAO {

    /**
     * Finds a user by unique identifier.
     *
     * @param id the user ID
     * @return Optional containing the User if found
     * @throws SQLException on database access error
     */
    Optional<User> findById(Long id) throws SQLException;

    /**
     * Finds a user by unique email address.
     *
     * @param email the user email
     * @return Optional containing the User if found
     * @throws SQLException on database access error
     */
    Optional<User> findByEmail(String email) throws SQLException;

    /**
     * Inserts a new user record.
     *
     * @param user the User to create
     * @return newly created User with generated ID
     * @throws SQLException on database access error
     */
    User create(User user) throws SQLException;

    /**
     * Updates an existing user record.
     *
     * @param user the User to update
     * @return true if updated, false otherwise
     * @throws SQLException on database access error
     */
    boolean update(User user) throws SQLException;

    /**
     * Deletes a user by ID.
     *
     * @param id the user ID
     * @return true if deleted, false otherwise
     * @throws SQLException on database access error
     */
    boolean delete(Long id) throws SQLException;

    /**
     * Retrieves all registered users in the system.
     *
     * @return list of all users
     * @throws SQLException on database access error
     */
    List<User> findAll() throws SQLException;
}
