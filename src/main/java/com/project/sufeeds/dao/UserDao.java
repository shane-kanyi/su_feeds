package com.project.sufeeds.dao;

import com.project.sufeeds.db.DatabaseManager;
import com.project.sufeeds.model.User;
import org.mindrot.jbcrypt.BCrypt; // Import the jBCrypt library

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDao {

    public User login(String username, String password) {
        // Step 1: SQL now only fetches user data based on the username.
        String sql = "SELECT user_id, username, password_hash FROM tbl_users WHERE username = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();

            // Step 2: Check if a user with that username was found.
            if (rs.next()) {
                String storedHash = rs.getString("password_hash");

                // Step 3: Use BCrypt to check if the provided password matches the stored hash.
                if (BCrypt.checkpw(password, storedHash)) {
                    // Password is correct, return the user object.
                    return new User(rs.getInt("user_id"), rs.getString("username"));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        // Return null if username not found or password does not match.
        return null;
    }

    public boolean register(String username, String password) {
        // Step 1: Generate a salt and hash the user's password.
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

        String sql = "INSERT INTO tbl_users(username, password_hash) VALUES(?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            // Step 2: Store the HASHED password, not the plain text one.
            pstmt.setString(2, hashedPassword);

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            if (e.getSQLState().equals("23505")) {
                System.err.println("Username already exists.");
            } else {
                e.printStackTrace();
            }
            return false;
        }
    }
}