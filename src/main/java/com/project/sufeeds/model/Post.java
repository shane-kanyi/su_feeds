package com.project.sufeeds.model;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

public class Post {
    private int postId;
    private int topicId;
    private int userId;
    private String postType;
    private String content;
    private OffsetDateTime createdAt;

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

    // Setters
    public void setPostId(int postId) { this.postId = postId; }
    public void setTopicId(int topicId) { this.topicId = topicId; }
    public void setUserId(int userId) { this.userId = userId; }
    public void setPostType(String postType) { this.postType = postType; }
    public void setContent(String content) { this.content = content; }
    public void setCreatedAt(OffsetDateTime createdAt) { this.createdAt = createdAt; }

    /**
     * Overrides the default toString method to provide a user-friendly representation
     * of the Post object, including a formatted timestamp adjusted to the user's
     * local time zone.
     *
     * @return A formatted string for display in the UI.
     */
    @Override
    public String toString() {
        String formattedDate = " (just now)"; // Default text if the timestamp is not available

        if (createdAt != null) {
            // Step 1: Get the system's default time zone (e.g., Africa/Nairobi for EAT).
            ZoneId localZone = ZoneId.systemDefault();

            // Step 2: Convert the UTC-based OffsetDateTime to a time zone-aware ZonedDateTime.
            ZonedDateTime localTime = createdAt.atZoneSameInstant(localZone);

            // Step 3: Create a formatter for a common, readable style (e.g., "8/20/25, 1:15 PM").
            DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT);

            // Step 4: Format the date and time using the formatter.
            formattedDate = " (at " + localTime.format(formatter) + ")";
        }

        // Combine all information into a single string for the JList.
        return String.format("[%s] %s... - by User %d%s",
                postType.toUpperCase(),
                content.substring(0, Math.min(content.length(), 40)),
                userId,
                formattedDate
        );
    }
}