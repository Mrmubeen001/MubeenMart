package com.mubeen.mubeenmart.service.impl;

import com.mubeen.mubeenmart.ai.ChatProvider;
import com.mubeen.mubeenmart.ai.ChatRateLimiter;
import com.mubeen.mubeenmart.ai.GeminiChatProvider;
import com.mubeen.mubeenmart.ai.MockChatProvider;
import com.mubeen.mubeenmart.exception.AppException;
import com.mubeen.mubeenmart.exception.ValidationException;
import com.mubeen.mubeenmart.service.ChatService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpSession;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

/**
 * Service implementation for the AI Chatbot feature adhering to Section 11 & 17 specifications:
 * - Selected via config flag (ai.chatbot.provider=gemini|mock)
 * - Guardrail: Per-session rate limit (10 messages/minute)
 * - Guardrail: Input length cap (500 characters)
 * - Guardrail: In-memory per-session query cache
 * - Outbound timeout & fallback degradation
 */
public class ChatServiceImpl implements ChatService {

    private static final Logger logger = LoggerFactory.getLogger(ChatServiceImpl.class);
    private static final int MAX_INPUT_LENGTH = 500;
    private static final String SESSION_LIMITER_KEY = "chatRateLimiter";
    private static final String SESSION_CACHE_KEY = "chatQueryCache";

    private final ChatProvider chatProvider;

    public ChatServiceImpl() {
        this.chatProvider = initializeProvider();
    }

    public ChatServiceImpl(ChatProvider chatProvider) {
        this.chatProvider = chatProvider;
    }

    @Override
    public String processChat(HttpSession session, String message) throws AppException {
        // 1. Input validation & length cap guardrail
        if (message == null || message.trim().isEmpty()) {
            throw new ValidationException("Message cannot be empty");
        }
        String cleanMessage = message.trim();
        if (cleanMessage.length() > MAX_INPUT_LENGTH) {
            throw new ValidationException("Message exceeds maximum permitted length of " + MAX_INPUT_LENGTH + " characters");
        }

        // 2. Per-session rate limit guardrail (10 messages / minute)
        if (session != null) {
            ChatRateLimiter limiter = (ChatRateLimiter) session.getAttribute(SESSION_LIMITER_KEY);
            if (limiter == null) {
                limiter = new ChatRateLimiter(10, 60_000);
                session.setAttribute(SESSION_LIMITER_KEY, limiter);
            }
            if (!limiter.allowRequest()) {
                logger.warn("Chat rate limit exceeded for session ID: {}", session.getId());
                throw new AppException("You are sending messages too quickly. Rate limit is 10 messages per minute. Please try again shortly.", 429);
            }

            // 3. Repeated identical questions cached in-memory per session (Section 17 rule 4)
            @SuppressWarnings("unchecked")
            Map<String, String> cache = (Map<String, String>) session.getAttribute(SESSION_CACHE_KEY);
            if (cache == null) {
                cache = new HashMap<>();
                session.setAttribute(SESSION_CACHE_KEY, cache);
            }
            String cacheKey = cleanMessage.toLowerCase(Locale.ROOT);
            if (cache.containsKey(cacheKey)) {
                logger.info("Serving chatbot response from session cache for question: '{}'", cleanMessage);
                return cache.get(cacheKey);
            }

            // 4. Generate response through configured ChatProvider
            String reply = chatProvider.getReply(cleanMessage, "Marketplace Categories: Electronics, Fashion, Home, Books");
            cache.put(cacheKey, reply);
            return reply;
        }

        return chatProvider.getReply(cleanMessage, null);
    }

    private ChatProvider initializeProvider() {
        Properties props = new Properties();
        try (InputStream in = getClass().getClassLoader().getResourceAsStream("config.properties")) {
            if (in != null) {
                props.load(in);
            }
        } catch (Exception e) {
            logger.warn("Could not load config.properties for chatbot provider setup", e);
        }

        String provider = System.getenv("AI_CHATBOT_PROVIDER");
        if (provider == null || provider.trim().isEmpty()) {
            provider = props.getProperty("ai.chatbot.provider", "mock");
        }

        String apiKey = System.getenv("GEMINI_API_KEY");
        if (apiKey == null || apiKey.trim().isEmpty()) {
            apiKey = props.getProperty("ai.chatbot.gemini.apiKey", "");
        }

        if ("gemini".equalsIgnoreCase(provider.trim()) && !apiKey.isEmpty()) {
            logger.info("Initializing GeminiChatProvider with configured API key");
            return new GeminiChatProvider(apiKey);
        }

        logger.info("Initializing MockChatProvider for AI chatbot integration");
        return new MockChatProvider();
    }
}
