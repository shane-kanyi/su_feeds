package com.project.sufeeds.dao;

import com.project.sufeeds.db.DatabaseManager;
import com.project.sufeeds.model.Post;

import java.sql.*;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

public class PostDao {

    /**
     * Adds a new post to the database. The 'created_at' timestamp is handled
     * automatically by the database.
     *
     * @param post The Post object to add (createdAt can be null).
     * @return true if the insertion was successful, false otherwise.
     */
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
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Retrieves a list of all posts for a given topic, ordered by the most recent.
     * It correctly fetches the 'created_at' timestamp as an OffsetDateTime object.
     *
     * @param topicId The ID of the topic to get posts for.
     * @return A list of Post objects.
     */
    public List<Post> getPostsByTopic(int topicId) {
        List<Post> posts = new ArrayList<>();
        String sql = "SELECT p.post_id, p.topic_id, p.user_id, p.post_type, p.content, p.created_at, u.username " +
                "FROM tbl_posts p JOIN tbl_users u ON p.user_id = u.user_id " +
                "WHERE p.topic_id = ? ORDER BY p.created_at DESC";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, topicId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                // Key Step: Use getObject with the target class to let the JDBC driver
                // handle the conversion from SQL TIMESTAMP WITH TIME ZONE to Java's OffsetDateTime.
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

    /**
     * Updates an existing post in the database.
     *
     * @param post The Post object with updated information.
     * @return true if the update was successful, false otherwise.
     */
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

    /**
     * Deletes a post from the database by its ID.
     *
     * @param postId The ID of the post to delete.
     * @return true if the deletion was successful, false otherwise.
     */
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