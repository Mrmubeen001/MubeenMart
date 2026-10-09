package com.mubeen.mubeenmart.controller;

import com.mubeen.mubeenmart.dto.ApiResponse;
import com.mubeen.mubeenmart.dto.LoginRequestDTO;
import com.mubeen.mubeenmart.dto.RegisterRequestDTO;
import com.mubeen.mubeenmart.dto.UserResponseDTO;
import com.mubeen.mubeenmart.exception.AppException;
import com.mubeen.mubeenmart.model.Role;
import com.mubeen.mubeenmart.model.User;
import com.mubeen.mubeenmart.service.UserService;
import com.mubeen.mubeenmart.service.impl.UserServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Controller servlet for authentication: login, registration, and session management.
 * Enforces mandatory engineering rules:
 * - Session fixation protection via session ID regeneration on login
 * - Explicit session timeout configuration
 */
@WebServlet(urlPatterns = {"/auth/*", "/login", "/register", "/logout"})
public class AuthServlet extends BaseServlet {

    private static final Logger logger = LoggerFactory.getLogger(AuthServlet.class);
    private static final int SESSION_TIMEOUT_SECONDS = 30 * 60; // 30 minutes explicit timeout

    private final UserService userService;

    public AuthServlet() {
        this(new UserServiceImpl());
    }

    public AuthServlet(UserService userService) {
        this.userService = userService;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String servletPath = request.getServletPath();

        if ("/logout".equals(servletPath)) {
            handleLogout(request, response);
            return;
        }

        if ("/register".equals(servletPath)) {
            request.getRequestDispatcher("/WEB-INF/views/auth/register.jsp").forward(request, response);
            return;
        }

        // Default to login page
        request.setAttribute("redirectUrl", request.getParameter("redirect"));
        request.getRequestDispatcher("/WEB-INF/views/auth/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String servletPath = request.getServletPath();
        String pathInfo = request.getPathInfo();

        boolean isRegister = "/register".equals(servletPath) || "/register".equals(pathInfo);
        boolean isLogin = "/login".equals(servletPath) || "/login".equals(pathInfo);

        if (isRegister) {
            handleRegister(request, response);
        } else if (isLogin) {
            handleLogin(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private void handleLogin(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String email;
        String password;
        String redirect = request.getParameter("redirect");

        if (isJsonRequest(request)) {
            LoginRequestDTO dto = readJsonBody(request, LoginRequestDTO.class);
            email = dto != null ? dto.getEmail() : null;
            password = dto != null ? dto.getPassword() : null;
        } else {
            email = request.getParameter("email");
            password = request.getParameter("password");
        }

        try {
            User user = userService.login(email, password);

            // Mandatory Engineering Rule 3: Session management via HttpSession.
            // Ensure session exists and regenerate session ID on login to protect against session fixation attacks.
            HttpSession session = request.getSession(true);
            request.changeSessionId();
            session.setMaxInactiveInterval(SESSION_TIMEOUT_SECONDS);
            session.setAttribute("currentUser", user);

            logger.info("Session established and ID regenerated for user: {}", user.getEmail());

            if (isJsonRequest(request)) {
                sendJsonResponse(response, HttpServletResponse.SC_OK,
                        ApiResponse.success(UserResponseDTO.fromEntity(user)));
            } else {
                if (redirect != null && !redirect.trim().isEmpty() && !redirect.contains("login")) {
                    response.sendRedirect(request.getContextPath() + redirect);
                } else if (user.getRole() == Role.ADMIN) {
                    response.sendRedirect(request.getContextPath() + "/admin/dashboard");
                } else if (user.getRole() == Role.SELLER) {
                    response.sendRedirect(request.getContextPath() + "/seller/dashboard");
                } else {
                    response.sendRedirect(request.getContextPath() + "/home");
                }
            }
        } catch (AppException e) {
            logger.warn("Login failed for {}: {}", email, e.getMessage());
            if (isJsonRequest(request)) {
                sendJsonResponse(response, e.getStatusCode(), ApiResponse.error("AUTH_ERROR", e.getMessage()));
            } else {
                request.setAttribute("errorMessage", e.getMessage());
                request.setAttribute("email", email);
                request.setAttribute("redirectUrl", redirect);
                request.getRequestDispatcher("/WEB-INF/views/auth/login.jsp").forward(request, response);
            }
        } catch (Exception e) {
            logger.error("Unexpected error during login for {}", email, e);
            if (isJsonRequest(request)) {
                sendJsonResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                        ApiResponse.error("SERVER_ERROR", "Login encountered an unexpected error. Please try again."));
            } else {
                request.setAttribute("errorMessage", "Login failed due to a server error. Please try again.");
                request.setAttribute("email", email);
                request.setAttribute("redirectUrl", redirect);
                request.getRequestDispatcher("/WEB-INF/views/auth/login.jsp").forward(request, response);
            }
        }
    }

    private void handleRegister(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        RegisterRequestDTO dto;
        if (isJsonRequest(request)) {
            dto = readJsonBody(request, RegisterRequestDTO.class);
        } else {
            dto = new RegisterRequestDTO(
                    request.getParameter("name"),
                    request.getParameter("email"),
                    request.getParameter("password"),
                    request.getParameter("role")
            );
        }

        try {
            UserResponseDTO registered = userService.register(dto);

            if (isJsonRequest(request)) {
                sendJsonResponse(response, HttpServletResponse.SC_CREATED, ApiResponse.success(registered));
            } else {
                request.setAttribute("successMessage", "Account created successfully! Please log in.");
                request.getRequestDispatcher("/WEB-INF/views/auth/login.jsp").forward(request, response);
            }
        } catch (AppException e) {
            logger.warn("Registration failed: {}", e.getMessage());
            if (isJsonRequest(request)) {
                sendJsonResponse(response, e.getStatusCode(), ApiResponse.error("REGISTRATION_ERROR", e.getMessage()));
            } else {
                request.setAttribute("errorMessage", e.getMessage());
                request.setAttribute("name", dto.getName());
                request.setAttribute("email", dto.getEmail());
                request.setAttribute("role", dto.getRole());
                request.getRequestDispatcher("/WEB-INF/views/auth/register.jsp").forward(request, response);
            }
        }
    }

    private void handleLogout(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        response.sendRedirect(request.getContextPath() + "/login?loggedOut=true");
    }
}
