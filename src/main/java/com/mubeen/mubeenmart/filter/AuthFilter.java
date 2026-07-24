package com.mubeen.mubeenmart.filter;

import com.mubeen.mubeenmart.dto.ApiResponse;
import com.mubeen.mubeenmart.model.Role;
import com.mubeen.mubeenmart.model.User;
import com.mubeen.mubeenmart.util.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Security filter enforcing session verification and role-based access control (RBAC).
 * Enforces security requirement:
 * "All protected servlets enforce session checks via AuthFilter."
 */
@WebFilter("/*")
public class AuthFilter implements Filter {

    private static final Logger logger = LoggerFactory.getLogger(AuthFilter.class);

    @Override
    public void init(FilterConfig filterConfig) {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        if (!(request instanceof HttpServletRequest) || !(response instanceof HttpServletResponse)) {
            chain.doFilter(request, response);
            return;
        }

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String contextPath = httpRequest.getContextPath();
        String path = httpRequest.getRequestURI().substring(contextPath.length());

        // Check if path is protected
        boolean requiresAdmin = path.startsWith("/admin");
        boolean requiresSeller = path.startsWith("/seller");
        boolean requiresBuyerAuth = path.startsWith("/checkout") || 
                                    path.startsWith("/orders") || 
                                    path.startsWith("/wishlist") ||
                                    path.startsWith("/reviews/submit") ||
                                    path.startsWith("/api/v1/checkout") ||
                                    path.startsWith("/api/v1/orders");

        if (!requiresAdmin && !requiresSeller && !requiresBuyerAuth) {
            // Public route, proceed
            chain.doFilter(request, response);
            return;
        }

        HttpSession session = httpRequest.getSession(false);
        User currentUser = session != null ? (User) session.getAttribute("currentUser") : null;

        // 1. Unauthenticated check
        if (currentUser == null) {
            logger.warn("Unauthenticated access attempt to protected route: {}", path);
            if (isJsonRequest(httpRequest)) {
                sendJsonError(httpResponse, HttpServletResponse.SC_UNAUTHORIZED, "UNAUTHORIZED", "Authentication required");
            } else {
                httpResponse.sendRedirect(httpRequest.getContextPath() + "/login?redirect=" + java.net.URLEncoder.encode(path, "UTF-8"));
            }
            return;
        }

        // 2. Role authorization check
        if (requiresAdmin && currentUser.getRole() != Role.ADMIN) {
            logger.warn("Unauthorized admin access attempt by user {}: path={}", currentUser.getEmail(), path);
            if (isJsonRequest(httpRequest)) {
                sendJsonError(httpResponse, HttpServletResponse.SC_FORBIDDEN, "FORBIDDEN", "Administrative privilege required");
            } else {
                httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN, "Administrative privilege required");
            }
            return;
        }

        if (requiresSeller && currentUser.getRole() != Role.SELLER && currentUser.getRole() != Role.ADMIN) {
            logger.warn("Unauthorized seller portal access attempt by user {}: path={}", currentUser.getEmail(), path);
            if (isJsonRequest(httpRequest)) {
                sendJsonError(httpResponse, HttpServletResponse.SC_FORBIDDEN, "FORBIDDEN", "Seller privileges required");
            } else {
                httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN, "Seller privileges required");
            }
            return;
        }

        // User authenticated and authorized
        chain.doFilter(request, response);
    }

    private boolean isJsonRequest(HttpServletRequest request) {
        String accept = request.getHeader("Accept");
        String uri = request.getRequestURI();
        return (accept != null && accept.contains("application/json")) || uri.contains("/api/");
    }

    private void sendJsonError(HttpServletResponse response, int status, String code, String message) throws IOException {
        response.setStatus(status);
        response.setContentType("application/json;charset=UTF-8");
        ApiResponse<Void> errorResponse = ApiResponse.error(code, message);
        response.getWriter().write(JsonUtil.toJson(errorResponse));
    }

    @Override
    public void destroy() {
    }
}
