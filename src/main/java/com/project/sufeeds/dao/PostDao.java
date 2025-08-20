package com.project.sufeeds.dao;

import com.project.sufeeds.db.DatabaseManager;
import com.project.sufeeds.model.Post;

import java.sql.*;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

public class PostDao {

    public boolean addPost(Post post) {
        String sql = "INSERT INTO tbl_posts(topic_id, user_id, post_type, content) VALUES(?, ?, ?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setInt(1, post.getTopicId());
            pstmt.setInt(2, post.getUserId());
            pstmt.setString(3, post.getPostType());
            pstmt.setString(4, post.getContent());

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        post.setPostId(generatedKeys.getInt(1));
                        // Set the generated creation timestamp if needed
                        post.setCreatedAt(generatedKeys.getTimestamp("created_at").toInstant().atOffset(ZoneOffset.UTC)); // Requires more import
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<Post> getPostsByTopic(int topicId) {
        List<Post> posts = new ArrayList<>();
        // Join with tbl_users to get the username for display
        String sql = "SELECT p.post_id, p.topic_id, p.user_id, p.post_type, p.content, p.created_at, u.username " +
                "FROM tbl_posts p JOIN tbl_users u ON p.user_id = u.user_id " +
                "WHERE p.topic_id = ? ORDER BY p.created_at DESC";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, topicId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                // For 'created_at' which is TIMESTAMP WITH TIME ZONE
                OffsetDateTime createdAt = rs.getObject("created_at", OffsetDateTime.class);
                posts.add(new Post(
                        rs.getInt("post_id"),
                        rs.getInt("topic_id"),
                        rs.getInt("user_id"),
                        rs.getString("post_type"),
                        rs.getString("content"),
                        createdAt
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return posts;
    }

    public boolean updatePost(Post post) {
        String sql = "UPDATE tbl_posts SET post_type = ?, content = ? WHERE post_id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, post.getPostType());
            pstmt.setString(2, post.getContent());
            pstmt.setInt(3, post.getPostId());

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deletePost(int postId) {
        String sql = "DELETE FROM tbl_posts WHERE post_id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, postId);
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}