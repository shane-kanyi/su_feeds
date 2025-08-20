package com.project.sufeeds.model;

public class User {
    private int userId;
    private String username;

    // Constructors
    public User(int userId, String username) {
        this.userId = userId;
        this.username = username;
    }

    // Getters and Setters
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
}