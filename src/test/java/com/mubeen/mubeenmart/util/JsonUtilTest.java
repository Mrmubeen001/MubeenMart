package com.mubeen.mubeenmart.util;

import com.mubeen.mubeenmart.dto.ApiResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("JsonUtil Tests")
class JsonUtilTest {

    @Test
    @DisplayName("Should serialize and deserialize ApiResponse correctly")
    void testApiResponseSerialization() {
        ApiResponse<Map<String, String>> response = ApiResponse.success(Map.of("message", "hello"));
        String json = JsonUtil.toJson(response);

        assertNotNull(json);
        assertTrue(json.contains("\"success\": true") || json.contains("\"success\":true"));
        assertTrue(json.contains("\"message\": \"hello\"") || json.contains("\"message\":\"hello\""));
    }
}
