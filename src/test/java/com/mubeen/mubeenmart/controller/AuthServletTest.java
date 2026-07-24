package com.mubeen.mubeenmart.controller;

import com.mubeen.mubeenmart.model.Role;
import com.mubeen.mubeenmart.model.User;
import com.mubeen.mubeenmart.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("AuthServlet Mockito Controller Tests")
class AuthServletTest {

    @Mock
    private UserService userService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private HttpSession session;

    @Mock
    private RequestDispatcher requestDispatcher;

    private AuthServlet authServlet;

    @BeforeEach
    void setUp() {
        authServlet = new AuthServlet(userService);
    }

    @Test
    @DisplayName("Should forward to login.jsp on GET /login")
    void testGetLogin() throws Exception {
        when(request.getServletPath()).thenReturn("/login");
        when(request.getParameter("redirect")).thenReturn(null);
        when(request.getRequestDispatcher("/WEB-INF/views/auth/login.jsp")).thenReturn(requestDispatcher);

        authServlet.doGet(request, response);

        verify(requestDispatcher).forward(request, response);
    }

    @Test
    @DisplayName("Should authenticate user, regenerate session ID, and redirect on valid POST /login")
    void testPostLoginSuccess() throws Exception {
        when(request.getServletPath()).thenReturn("/login");
        when(request.getHeader("Accept")).thenReturn("text/html");
        when(request.getContentType()).thenReturn("application/x-www-form-urlencoded");
        when(request.getParameter("email")).thenReturn("buyer@mubeenmart.com");
        when(request.getParameter("password")).thenReturn("Password@123");
        when(request.getParameter("redirect")).thenReturn(null);
        when(request.getContextPath()).thenReturn("/mubeenmart");
        when(request.getSession(true)).thenReturn(session);

        User authenticated = new User(1L, "Mohamed Buyer", "buyer@mubeenmart.com", "hash", Role.BUYER, LocalDateTime.now());
        when(userService.login("buyer@mubeenmart.com", "Password@123")).thenReturn(authenticated);

        authServlet.doPost(request, response);

        // Verify session fixation defense: regenerate session ID
        verify(request).changeSessionId();
        verify(session).setMaxInactiveInterval(30 * 60);
        verify(session).setAttribute("currentUser", authenticated);
        verify(response).sendRedirect("/mubeenmart/home");
    }

    @Test
    @DisplayName("Should invalidate session on GET /logout")
    void testGetLogout() throws Exception {
        when(request.getServletPath()).thenReturn("/logout");
        when(request.getContextPath()).thenReturn("/mubeenmart");
        when(request.getSession(false)).thenReturn(session);

        authServlet.doGet(request, response);

        verify(session).invalidate();
        verify(response).sendRedirect("/mubeenmart/login?loggedOut=true");
    }
}
