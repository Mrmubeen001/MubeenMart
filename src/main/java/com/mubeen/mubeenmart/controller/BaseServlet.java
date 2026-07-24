package com.mubeen.mubeenmart.controller;

import com.mubeen.mubeenmart.dto.ApiResponse;
import com.mubeen.mubeenmart.model.User;
import com.mubeen.mubeenmart.util.JsonUtil;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Base abstract servlet providing common HTTP orchestration and JSON helpers.
 */
public abstract class BaseServlet extends HttpServlet {

    protected <T> void sendJsonResponse(HttpServletResponse response, int statusCode, ApiResponse<T> envelope)
            throws IOException {
        response.setStatus(statusCode);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(JsonUtil.toJson(envelope));
    }

    protected <T> T readJsonBody(HttpServletRequest request, Class<T> clazz) throws IOException {
        return JsonUtil.fromJson(request.getReader(), clazz);
    }

    protected User getCurrentUser(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            return (User) session.getAttribute("currentUser");
        }
        return null;
    }

    protected boolean isJsonRequest(HttpServletRequest request) {
        String accept = request.getHeader("Accept");
        String contentType = request.getContentType();
        return (accept != null && accept.contains("application/json")) ||
               (contentType != null && contentType.contains("application/json"));
    }
}
