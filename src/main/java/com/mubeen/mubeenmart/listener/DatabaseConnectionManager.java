package com.mubeen.mubeenmart.listener;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Thread-safe DataSource holder initialized and managed solely by {@link AppContextListener}.
 * Enforces mandatory engineering rule:
 * "Connection pool lifecycle owned by a single ServletContextListener. No DriverManager.getConnection() calls outside this listener."
 */
public final class DatabaseConnectionManager {

    private static volatile DataSource dataSource;

    private DatabaseConnectionManager() {
        // Prevent instantiation
    }

    /**
     * Sets the shared DataSource. Only called by AppContextListener during application startup.
     *
     * @param ds the configured HikariDataSource
     */
    static void setDataSource(DataSource ds) {
        dataSource = ds;
    }

    /**
     * Gets the shared DataSource.
     *
     * @return the active DataSource
     */
    public static DataSource getDataSource() {
        if (dataSource == null) {
            throw new IllegalStateException("DataSource has not been initialized by AppContextListener");
        }
        return dataSource;
    }

    /**
     * Obtains a connection from the pool.
     *
     * @return active database connection
     * @throws SQLException if a connection cannot be acquired
     */
    public static Connection getConnection() throws SQLException {
        return getDataSource().getConnection();
    }
}
