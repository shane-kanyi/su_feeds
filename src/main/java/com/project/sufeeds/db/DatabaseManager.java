package com.project.sufeeds.db;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseManager {
    private static Properties properties = new Properties();

    static {
        // This static block runs once when the class is loaded
        try (InputStream input = DatabaseManager.class.getClassLoader().getResourceAsStream("application.properties")) {
            if (input == null) {
                System.err.println("Sorry, unable to find application.properties");
                // Handle this error appropriately, perhaps throw an unchecked exception
                throw new IOException("application.properties not found!");
            }
            properties.load(input);
        } catch (IOException ex) {
            ex.printStackTrace();
            // Handle this critical error
            throw new RuntimeException("Failed to load database properties: " + ex.getMessage());
        }
    }

    public static Connection getConnection() throws SQLException {
        String url = properties.getProperty("db.url");
        String user = properties.getProperty("db.user");
        String password = properties.getProperty("db.password"); // Plain text for simplicity, use secure methods in production!

        return DriverManager.getConnection(url, user, password);
    }
}