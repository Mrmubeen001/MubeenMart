package com.mubeen.mubeenmart.listener;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;

/**
 * Single application lifecycle listener that manages the HikariCP connection pool lifecycle.
 * Conforms to engineering rule: Connection pool lifecycle owned by a single ServletContextListener.
 */
@WebListener
public class AppContextListener implements ServletContextListener {

    private static final Logger logger = LoggerFactory.getLogger(AppContextListener.class);
    private HikariDataSource dataSource;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        logger.info("Initializing MubeenMart application context and HikariCP connection pool...");
        ServletContext context = sce.getServletContext();

        Properties props = loadConfiguration();

        HikariConfig config = new HikariConfig();
        String jdbcUrl = getPropertyOrEnv(props, "db.url", "DB_URL", "jdbc:h2:mem:mubeenmart;DB_CLOSE_DELAY=-1");
        String user = getPropertyOrEnv(props, "db.user", "DB_USER", "sa");
        String password = getPropertyOrEnv(props, "db.password", "DB_PASSWORD", "");
        String driver = getPropertyOrEnv(props, "db.driver", "DB_DRIVER", "org.h2.Driver");

        config.setJdbcUrl(jdbcUrl);
        config.setUsername(user);
        config.setPassword(password);
        config.setDriverClassName(driver);
        config.setPoolName("MubeenMartHikariPool");

        int maxPoolSize = Integer.parseInt(getPropertyOrEnv(props, "hikari.maximumPoolSize", "HIKARI_MAX_POOL_SIZE", "10"));
        int minIdle = Integer.parseInt(getPropertyOrEnv(props, "hikari.minimumIdle", "HIKARI_MIN_IDLE", "2"));
        config.setMaximumPoolSize(maxPoolSize);
        config.setMinimumIdle(minIdle);

        dataSource = new HikariDataSource(config);
        DatabaseConnectionManager.setDataSource(dataSource);
        context.setAttribute("dataSource", dataSource);

        logger.info("HikariCP connection pool initialized successfully with URL: {}", jdbcUrl);

        // Run database migration and schema setup if needed
        initializeDatabase(dataSource);

        // Store config flags in context
        String aiProvider = getPropertyOrEnv(props, "ai.chatbot.provider", "AI_CHATBOT_PROVIDER", "mock");
        context.setAttribute("aiChatbotProvider", aiProvider);
        logger.info("MubeenMart context initialized. AI Chatbot Provider: {}", aiProvider);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        logger.info("Closing HikariCP connection pool...");
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
            logger.info("HikariCP connection pool successfully shut down.");
        }
        DatabaseConnectionManager.setDataSource(null);
    }

    private Properties loadConfiguration() {
        Properties props = new Properties();
        try (InputStream in = getClass().getClassLoader().getResourceAsStream("config.properties")) {
            if (in != null) {
                props.load(in);
            }
        } catch (Exception e) {
            logger.warn("Could not load config.properties from classpath, falling back to defaults/env", e);
        }
        return props;
    }

    private String getPropertyOrEnv(Properties props, String propKey, String envKey, String defaultValue) {
        String envVal = System.getenv(envKey);
        if (envVal != null && !envVal.trim().isEmpty()) {
            return envVal.trim();
        }
        String propVal = props.getProperty(propKey);
        if (propVal != null && !propVal.trim().isEmpty()) {
            return propVal.trim();
        }
        return defaultValue;
    }

    private void initializeDatabase(HikariDataSource ds) {
        try (Connection conn = ds.getConnection()) {
            boolean tablesExist = false;
            try (ResultSet rs = conn.getMetaData().getTables(null, null, "USERS", null)) {
                if (rs.next()) {
                    tablesExist = true;
                }
            }

            if (!tablesExist) {
                logger.info("Tables not found. Applying database schema and seed data...");
                executeSqlScript(conn, "schema.sql");
                executeSqlScript(conn, "seed.sql");
                logger.info("Database schema and seed data applied successfully.");
            } else {
                logger.info("Database schema already initialized.");
            }
        } catch (Exception e) {
            logger.error("Failed to initialize database schema", e);
        }
    }

    private void executeSqlScript(Connection conn, String resourceName) {
        try (InputStream in = getClass().getClassLoader().getResourceAsStream(resourceName)) {
            if (in == null) {
                logger.warn("SQL resource not found on classpath: {}", resourceName);
                return;
            }
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
                 Statement stmt = conn.createStatement()) {
                StringBuilder sql = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    String trimmed = line.trim();
                    if (trimmed.startsWith("--") || trimmed.isEmpty()) {
                        continue;
                    }
                    sql.append(line).append("\n");
                    if (trimmed.endsWith(";")) {
                        String statementStr = sql.toString().replace(";", "").trim();
                        if (!statementStr.isEmpty()) {
                            stmt.execute(statementStr);
                        }
                        sql.setLength(0);
                    }
                }
            }
        } catch (Exception e) {
            logger.error("Error executing SQL script: {}", resourceName, e);
        }
    }
}
