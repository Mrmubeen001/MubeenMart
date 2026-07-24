package com.mubeen.mubeenmart.ai;

/**
 * Interface for AI chat providers conforming strictly to Section 17 specification:
 * public interface ChatProvider {
 *     String getReply(String userMessage, String context);
 * }
 */
public interface ChatProvider {

    /**
     * Generates an intelligent assistant reply for the user query within marketplace context.
     *
     * @param userMessage user's input prompt
     * @param context marketplace contextual information (categories, popular products)
     * @return generated assistant response string
     */
    String getReply(String userMessage, String context);
}
