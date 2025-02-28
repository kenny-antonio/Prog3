package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private final static int defaultPort = 5432; // Default PostgreSQL port
    private final String host = System.getenv("DB_HOST"); // Database host
    private final String user = System.getenv("DB_USER"); // Database username
    private final String password = System.getenv("DB_PASSWORD"); // Database password
    private final String database = System.getenv("DB_NAME"); // Database name
    private final String jdbcURL;

    public DatabaseConnection() {
        // Ensure environment variables are correctly retrieved
        if (host == null || user == null || password == null || database == null) {
            throw new IllegalStateException("One or more environment variables are missing.");
        }

        // Construct the JDBC URL with correct format
        jdbcURL = "jdbc:postgresql://" + host + ":" + defaultPort + "/" + database;
    }

    public Connection getConnection() {
        try {
            // Establish connection to the database
            Connection connection = DriverManager.getConnection(jdbcURL, user, password);
            System.out.println("Database connection successful.");
            return connection;
        } catch (SQLException e) {
            // Print error if connection fails
            System.err.println("Database connection failed.");
            e.printStackTrace();
            throw new RuntimeException("Error connecting to the database.", e);
        }
    }
}
