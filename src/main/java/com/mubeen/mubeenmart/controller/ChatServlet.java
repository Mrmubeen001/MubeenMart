package com.mubeen.mubeenmart.controller;

import com.google.gson.JsonObject;
import com.mubeen.mubeenmart.dto.ApiResponse;
import com.mubeen.mubeenmart.exception.AppException;
import com.mubeen.mubeenmart.service.ChatService;
import com.mubeen.mubeenmart.service.impl.ChatServiceImpl;
import com.mubeen.mubeenmart.util.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Map;

/**
 * Controller servlet for the AI Chatbot assistant endpoint.
 * Fulfills Section 11 & Section 17 specifications:
 * - Versioned under /api/v1/chat
 * - Validates input and enforces per-session rate limits
 * - Wraps LLM calls server-side and provides graceful degradation
 */
@WebServlet("/api/v1/chat")
public class ChatServlet extends BaseServlet {

    private static final Logger logger = LoggerFactory.getLogger(ChatServlet.class);
    private final ChatService chatService;

    public ChatServlet() {
        this(new ChatServiceImpl());
    }

    public ChatServlet(ChatService chatService) {
        this.chatService = chatService;
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(true);
        String message = null;

        try {
            JsonObject body = readJsonBody(request, JsonObject.class);
            if (body != null && body.has("message")) {
                message = body.get("message").getAsString();
            }
        } catch (Exception e) {
            message = request.getParameter("message");
        }

        try {
            String reply = chatService.processChat(session, message);
            sendJsonResponse(response, HttpServletResponse.SC_OK,
                    ApiResponse.success(Map.of("reply", reply)));
        } catch (AppException e) {
            logger.warn("Chat request rejected: {}", e.getMessage());
            sendJsonResponse(response, e.getStatusCode(),
                    ApiResponse.error("CHAT_ERROR", e.getMessage()));
        } catch (Exception e) {
            logger.error("Unexpected error in ChatServlet", e);
            // Section 11 Rule 3: Static degraded response instead of error page
            sendJsonResponse(response, HttpServletResponse.SC_OK,
                    ApiResponse.success(Map.of("reply", "I am currently running in offline support mode. You can ask about product categories, shipping terms, returns, or seller registration!")));
        }
    }
}
