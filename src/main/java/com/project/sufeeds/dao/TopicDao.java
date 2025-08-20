package com.project.sufeeds.dao;

import com.project.sufeeds.db.DatabaseManager;
import com.project.sufeeds.model.Topic;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TopicDao {

    public boolean addTopic(Topic topic) {
        String sql = "INSERT INTO tbl_topics(course_id, week_number, title, description) VALUES(?, ?, ?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setInt(1, topic.getCourseId());
            pstmt.setInt(2, topic.getWeekNumber());
            pstmt.setString(3, topic.getTitle());
            pstmt.setString(4, topic.getDescription());

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        topic.setTopicId(generatedKeys.getInt(1));
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<Topic> getTopicsByCourse(int courseId) {
        List<Topic> topics = new ArrayList<>();
        String sql = "SELECT topic_id, course_id, week_number, title, description FROM tbl_topics WHERE course_id = ? ORDER BY week_number, title";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, courseId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                topics.add(new Topic(
                        rs.getInt("topic_id"),
                        rs.getInt("course_id"),
                        rs.getInt("week_number"),
                        rs.getString("title"),
                        rs.getString("description")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return topics;
    }

    public Topic getTopicById(int topicId) {
        String sql = "SELECT topic_id, course_id, week_number, title, description FROM tbl_topics WHERE topic_id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, topicId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new Topic(
                        rs.getInt("topic_id"),
                        rs.getInt("course_id"),
                        rs.getInt("week_number"),
                        rs.getString("title"),
                        rs.getString("description")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean updateTopic(Topic topic) {
        String sql = "UPDATE tbl_topics SET course_id = ?, week_number = ?, title = ?, description = ? WHERE topic_id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, topic.getCourseId());
            pstmt.setInt(2, topic.getWeekNumber());
            pstmt.setString(3, topic.getTitle());
            pstmt.setString(4, topic.getDescription());
            pstmt.setInt(5, topic.getTopicId());

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteTopic(int topicId) {
        String sql = "DELETE FROM tbl_topics WHERE topic_id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, topicId);
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}