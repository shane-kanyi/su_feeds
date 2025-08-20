package com.project.sufeeds.model;

import java.time.OffsetDateTime; // Use OffsetDateTime for TIMESTAMP WITH TIME ZONE

public class Post {
    private int postId;
    private int topicId;
    private int userId;
    private String postType;
    private String content;
    private OffsetDateTime createdAt; // Use OffsetDateTime for TIMESTAMP WITH TIME ZONE

    public Post(int postId, int topicId, int userId, String postType, String content, OffsetDateTime createdAt) {
        this.postId = postId;
        this.topicId = topicId;
        this.userId = userId;
        this.postType = postType;
        this.content = content;
        this.createdAt = createdAt;
    }

    // Getters
    public int getPostId() { return postId; }
    public int getTopicId() { return topicId; }
    public int getUserId() { return userId; }
    public String getPostType() { return postType; }
    public String getContent() { return content; }
    public OffsetDateTime getCreatedAt() { return createdAt; }

    // Setters (if needed)
    public void setPostId(int postId) { this.postId = postId; }
    public void setTopicId(int topicId) { this.topicId = topicId; }
    public void setUserId(int userId) { this.userId = userId; }
    public void setPostType(String postType) { this.postType = postType; }
    public void setContent(String content) { this.content = content; }
    public void setCreatedAt(OffsetDateTime createdAt) { this.createdAt = createdAt; }

    @Override
    public String toString() {
        // You might want to display the author's username here too for a more user-friendly output
        return "[" + postType + "] " + content.substring(0, Math.min(content.length(), 50)) + "... (by User " + userId + ")";
    }
}