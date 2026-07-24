package com.mubeen.mubeenmart.ai;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

/**
 * Gemini AI chat provider calling Google's Generative AI endpoint server-side.
 * Enforces server-side API key storage, request timeout, fixed prompt template, and graceful fallback.
 */
public class GeminiChatProvider implements ChatProvider {

    private static final Logger logger = LoggerFactory.getLogger(GeminiChatProvider.class);
    private static final String API_ENDPOINT = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent?key=";
    private static final String SYSTEM_INSTRUCTION =
            "You are MubeenMart AI, a friendly and helpful customer assistant for MubeenMart—an e-commerce marketplace built by Mohamed Mubeen. " +
            "Your scope is strictly restricted to helping customers with product inquiries, order tracking, shipping, seller onboarding, mock payments, and returns. " +
            "Keep your answers concise, accurate, and professional. Never discuss unrelated topics.";

    private final String apiKey;
    private final HttpClient httpClient;
    private final Gson gson;
    private final MockChatProvider fallbackProvider;

    public GeminiChatProvider(String apiKey) {
        this.apiKey = apiKey != null ? apiKey.trim() : "";
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(4))
                .build();
        this.gson = new Gson();
        this.fallbackProvider = new MockChatProvider();
    }

    @Override
    public String getReply(String userMessage, String context) {
        if (apiKey.isEmpty()) {
            logger.warn("Gemini API key is not configured; using offline MockChatProvider fallback");
            return fallbackProvider.getReply(userMessage, context);
        }

        try {
            JsonObject requestBody = new JsonObject();
            
            // System instruction
            JsonObject systemObj = new JsonObject();
            JsonArray systemParts = new JsonArray();
            JsonObject systemPart = new JsonObject();
            systemPart.addProperty("text", SYSTEM_INSTRUCTION + (context != null ? " Current Context: " + context : ""));
            systemParts.add(systemPart);
            systemObj.add("parts", systemParts);
            requestBody.add("system_instruction", systemObj);

            // User prompt contents
            JsonArray contents = new JsonArray();
            JsonObject contentObj = new JsonObject();
            contentObj.addProperty("role", "user");
            JsonArray parts = new JsonArray();
            JsonObject part = new JsonObject();
            part.addProperty("text", userMessage);
            parts.add(part);
            contentObj.add("parts", parts);
            contents.add(contentObj);
            requestBody.add("contents", contents);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(API_ENDPOINT + apiKey))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(requestBody)))
                    .timeout(Duration.ofSeconds(5))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                JsonObject jsonResponse = gson.fromJson(response.body(), JsonObject.class);
                JsonArray candidates = jsonResponse.getAsJsonArray("candidates");
                if (candidates != null && !candidates.isEmpty()) {
                    JsonObject candidate = candidates.get(0).getAsJsonObject();
                    JsonObject content = candidate.getAsJsonObject("content");
                    JsonArray resParts = content.getAsJsonArray("parts");
                    if (resParts != null && !resParts.isEmpty()) {
                        return resParts.get(0).getAsJsonObject().get("text").getAsString().trim();
                    }
                }
            } else {
                logger.warn("Gemini API responded with HTTP {}: using fallback response", response.statusCode());
            }
        } catch (Exception e) {
            // Section 11 Requirement: "API call wrapped in try/catch. On failure, return a static degraded response instead of an error page."
            logger.error("Error communicating with Gemini API: {}", e.getMessage());
        }

        return fallbackProvider.getReply(userMessage, context);
    }
}
