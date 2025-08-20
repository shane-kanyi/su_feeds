package com.project.sufeeds.dao;

import com.project.sufeeds.db.DatabaseManager;
import com.project.sufeeds.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDao {

    public User login(String username, String password) {
        // In a real app, 'password' should be hashed and compared with 'password_hash'
        String sql = "SELECT user_id, username FROM tbl_users WHERE username = ? AND password_hash = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            pstmt.setString(2, password); // Plain text for simplicity, DO NOT DO THIS IN PRODUCTION

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new User(rs.getInt("user_id"), rs.getString("username"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Login failed
    }

    public boolean register(String username, String password) {
        String sql = "INSERT INTO tbl_users(username, password_hash) VALUES(?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            pstmt.setString(2, password); // Again, password should be hashed

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            // e.getSQLState().equals("23505") means unique violation
            if (e.getSQLState().equals("23505")) {
                System.err.println("Username already exists.");
            } else {
                e.printStackTrace();
            }
            return false;
        }
    }
}