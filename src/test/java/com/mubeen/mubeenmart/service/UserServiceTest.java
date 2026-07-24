package com.mubeen.mubeenmart.service;

import com.mubeen.mubeenmart.dao.UserDAO;
import com.mubeen.mubeenmart.dto.RegisterRequestDTO;
import com.mubeen.mubeenmart.dto.UserResponseDTO;
import com.mubeen.mubeenmart.exception.AppException;
import com.mubeen.mubeenmart.exception.AuthenticationException;
import com.mubeen.mubeenmart.exception.AuthorizationException;
import com.mubeen.mubeenmart.exception.ConflictException;
import com.mubeen.mubeenmart.model.Role;
import com.mubeen.mubeenmart.model.User;
import com.mubeen.mubeenmart.service.impl.UserServiceImpl;
import com.mubeen.mubeenmart.util.PasswordUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserService Unit Tests (Mockito Mocked DAO)")
class UserServiceTest {

    @Mock
    private UserDAO userDAO;

    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = new UserServiceImpl(userDAO);
    }

    @Test
    @DisplayName("Should register buyer successfully")
    void testRegisterSuccess() throws Exception {
        RegisterRequestDTO dto = new RegisterRequestDTO("Mubeen Test", "mubeen@test.com", "Password@123", "BUYER");
        when(userDAO.findByEmail("mubeen@test.com")).thenReturn(Optional.empty());

        User savedUser = new User(10L, "Mubeen Test", "mubeen@test.com", "hashed", Role.BUYER, LocalDateTime.now());
        when(userDAO.create(any(User.class))).thenReturn(savedUser);

        UserResponseDTO response = userService.register(dto);
        assertNotNull(response);
        assertEquals(10L, response.getId());
        assertEquals("Mubeen Test", response.getName());
        assertEquals(Role.BUYER, response.getRole());
    }

    @Test
    @DisplayName("Should reject admin public registration per F1")
    void testRegisterAdminRejected() {
        RegisterRequestDTO dto = new RegisterRequestDTO("Admin Attempter", "admin@fake.com", "Password@123", "ADMIN");
        assertThrows(AuthorizationException.class, () -> userService.register(dto));
        verifyNoInteractions(userDAO);
    }

    @Test
    @DisplayName("Should reject duplicate email registration")
    void testRegisterDuplicateEmail() throws Exception {
        RegisterRequestDTO dto = new RegisterRequestDTO("Existing", "exists@test.com", "Password@123", "BUYER");
        when(userDAO.findByEmail("exists@test.com"))
                .thenReturn(Optional.of(new User(1L, "Old", "exists@test.com", "h", Role.BUYER, LocalDateTime.now())));

        assertThrows(ConflictException.class, () -> userService.register(dto));
    }

    @Test
    @DisplayName("Should authenticate valid credentials")
    void testLoginSuccess() throws Exception {
        String hash = PasswordUtil.hashPassword("Password@123");
        User user = new User(1L, "Mubeen", "mubeen@test.com", hash, Role.BUYER, LocalDateTime.now());
        when(userDAO.findByEmail("mubeen@test.com")).thenReturn(Optional.of(user));

        User loggedIn = userService.login("mubeen@test.com", "Password@123");
        assertNotNull(loggedIn);
        assertEquals(1L, loggedIn.getId());
    }

    @Test
    @DisplayName("Should reject incorrect password")
    void testLoginInvalidPassword() throws Exception {
        String hash = PasswordUtil.hashPassword("Password@123");
        User user = new User(1L, "Mubeen", "mubeen@test.com", hash, Role.BUYER, LocalDateTime.now());
        when(userDAO.findByEmail("mubeen@test.com")).thenReturn(Optional.of(user));

        assertThrows(AuthenticationException.class, () -> userService.login("mubeen@test.com", "WrongPassword"));
    }
}
