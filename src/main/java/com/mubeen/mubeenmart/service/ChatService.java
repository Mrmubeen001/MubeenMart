package com.mubeen.mubeenmart.service;

import com.mubeen.mubeenmart.exception.AppException;

import javax.servlet.http.HttpSession;

/**
 * Service interface for the AI chatbot assistant feature (Phase 3 deliverable).
 */
public interface ChatService {

    /**
     * Processes a user chat inquiry with rate limiting, caching, and guardrail enforcement.
     *
     * @param session user HTTP session for per-session rate limiting and cache
     * @param message user message string
     * @return bot response reply string
     * @throws AppException if rate limit is exceeded or input validation fails
     */
    String processChat(HttpSession session, String message) throws AppException;
}
