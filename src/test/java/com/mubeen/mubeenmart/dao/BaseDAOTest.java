package com.mubeen.mubeenmart.dao;

import com.mubeen.mubeenmart.listener.DatabaseConnectionManager;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.Statement;

/**
 * Base test class establishing in-memory H2 database per Section 9 specification:
 * "DAO CRUD operations against schema: JUnit 5 against jdbc:h2:mem:test;DB_CLOSE_DELAY=-1 initialized from schema.sql"
 */
public abstract class BaseDAOTest {

    protected static HikariDataSource testDataSource;

    @BeforeAll
    public static void setUpDatabase() throws Exception {
        if (testDataSource == null || testDataSource.isClosed()) {
            HikariConfig config = new HikariConfig();
            config.setJdbcUrl("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1");
            config.setUsername("sa");
            config.setPassword("");
            config.setDriverClassName("org.h2.Driver");
            config.setMaximumPoolSize(5);

            testDataSource = new HikariDataSource(config);

            // Register DataSource using reflection or package method in DatabaseConnectionManager
            Method method = DatabaseConnectionManager.class.getDeclaredMethod("setDataSource", javax.sql.DataSource.class);
            method.setAccessible(true);
            method.invoke(null, testDataSource);

            // Initialize schema from test-schema.sql
            try (Connection conn = testDataSource.getConnection();
                 InputStream in = BaseDAOTest.class.getClassLoader().getResourceAsStream("test-schema.sql")) {
                if (in != null) {
                    try (BufferedReader reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
                         Statement stmt = conn.createStatement()) {
                        StringBuilder sql = new StringBuilder();
                        String line;
                        while ((line = reader.readLine()) != null) {
                            String trimmed = line.trim();
                            if (trimmed.startsWith("--") || trimmed.isEmpty()) continue;
                            sql.append(line).append("\n");
                            if (trimmed.endsWith(";")) {
                                String stmtStr = sql.toString().replace(";", "").trim();
                                if (!stmtStr.isEmpty()) {
                                    stmt.execute(stmtStr);
                                }
                                sql.setLength(0);
                            }
                        }
                    }
                }
            }
        }
    }

    @AfterAll
    public static void tearDownDatabase() {
        // Leave in-memory DB active for test suite duration or close
    }
}
