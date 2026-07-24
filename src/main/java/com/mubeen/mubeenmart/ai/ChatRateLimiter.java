package com.mubeen.mubeenmart.ai;

import java.util.LinkedList;
import java.util.Queue;

/**
 * In-memory sliding window rate limiter for chat sessions.
 * Enforces Section 17 guardrail: "per-session rate limit (10 messages/minute)".
 */
public class ChatRateLimiter {

    private final int maxMessages;
    private final long windowMillis;
    private final Queue<Long> timestamps = new LinkedList<>();

    public ChatRateLimiter() {
        this(10, 60_000); // 10 messages per 60 seconds
    }

    public ChatRateLimiter(int maxMessages, long windowMillis) {
        this.maxMessages = maxMessages;
        this.windowMillis = windowMillis;
    }

    public synchronized boolean allowRequest() {
        long now = System.currentTimeMillis();
        // Evict expired entries
        while (!timestamps.isEmpty() && now - timestamps.peek() > windowMillis) {
            timestamps.poll();
        }

        if (timestamps.size() < maxMessages) {
            timestamps.add(now);
            return true;
        }
        return false;
    }
}
