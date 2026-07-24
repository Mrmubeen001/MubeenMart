package com.mubeen.mubeenmart.dao;

import com.mubeen.mubeenmart.dao.impl.UserDAOImpl;
import com.mubeen.mubeenmart.model.Role;
import com.mubeen.mubeenmart.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("UserDAO CRUD Tests")
class UserDAOTest extends BaseDAOTest {

    private UserDAO userDAO;

    @BeforeEach
    void setUp() {
        userDAO = new UserDAOImpl();
    }

    @Test
    @DisplayName("Should create, find by ID and email, update and delete a user")
    void testUserCrudLifecycle() throws SQLException {
        String uniqueEmail = "user_" + UUID.randomUUID().toString().substring(0, 6) + "@mubeenmart.com";
        User user = new User(null, "Test User", uniqueEmail, "hashed_pw_xyz", Role.BUYER, LocalDateTime.now());

        // 1. Create
        User created = userDAO.create(user);
        assertNotNull(created.getId());
        assertEquals("Test User", created.getName());

        // 2. Find by ID
        Optional<User> byId = userDAO.findById(created.getId());
        assertTrue(byId.isPresent());
        assertEquals(uniqueEmail, byId.get().getEmail());

        // 3. Find by Email
        Optional<User> byEmail = userDAO.findByEmail(uniqueEmail);
        assertTrue(byEmail.isPresent());
        assertEquals(created.getId(), byEmail.get().getId());

        // 4. Update
        created.setName("Updated User Name");
        boolean updated = userDAO.update(created);
        assertTrue(updated);
        assertEquals("Updated User Name", userDAO.findById(created.getId()).get().getName());

        // 5. Delete
        boolean deleted = userDAO.delete(created.getId());
        assertTrue(deleted);
        assertFalse(userDAO.findById(created.getId()).isPresent());
    }
}
