package com.mubeen.mubeenmart.controller;

import com.mubeen.mubeenmart.dto.ApiResponse;
import com.mubeen.mubeenmart.listener.DatabaseConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Health check endpoint verifying system and database connectivity.
 * Conforms to Section 18 requirement:
 * "GET /api/v1/health returns { "status": "UP", "db": "UP" }, verifying database connectivity."
 */
@WebServlet("/api/v1/health")
public class HealthServlet extends BaseServlet {

    private static final Logger logger = LoggerFactory.getLogger(HealthServlet.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Map<String, String> health = new LinkedHashMap<>();
        health.put("status", "UP");

        boolean dbUp = false;
        try (Connection conn = DatabaseConnectionManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT 1")) {
            if (rs.next()) {
                dbUp = true;
            }
        } catch (Exception e) {
            logger.error("Health check database connectivity test failed", e);
        }

        health.put("db", dbUp ? "UP" : "DOWN");

        if (dbUp) {
            sendJsonResponse(response, HttpServletResponse.SC_OK, ApiResponse.success(health));
        } else {
            sendJsonResponse(response, HttpServletResponse.SC_SERVICE_UNAVAILABLE,
                    ApiResponse.error("SERVICE_UNAVAILABLE", "Database connection degraded", health));
        }
    }
}
