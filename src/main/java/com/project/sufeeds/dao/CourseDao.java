package com.project.sufeeds.dao;

import com.project.sufeeds.db.DatabaseManager;
import com.project.sufeeds.model.Course;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CourseDao {

    public boolean addCourse(Course course) {
        String sql = "INSERT INTO tbl_courses(course_code, course_name) VALUES(?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, course.getCourseCode());
            pstmt.setString(2, course.getCourseName());

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        course.setCourseId(generatedKeys.getInt(1));
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            if (e.getSQLState().equals("23505")) { // Unique violation for course_code
                System.err.println("Course with code " + course.getCourseCode() + " already exists.");
            } else {
                e.printStackTrace();
            }
        }
        return false;
    }

    public List<Course> getAllCourses() {
        List<Course> courses = new ArrayList<>();
        String sql = "SELECT course_id, course_code, course_name FROM tbl_courses ORDER BY course_code";
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                courses.add(new Course(
                        rs.getInt("course_id"),
                        rs.getString("course_code"),
                        rs.getString("course_name")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return courses;
    }

    public Course getCourseById(int id) {
        String sql = "SELECT course_id, course_code, course_name FROM tbl_courses WHERE course_id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new Course(
                        rs.getInt("course_id"),
                        rs.getString("course_code"),
                        rs.getString("course_name")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean updateCourse(Course course) {
        String sql = "UPDATE tbl_courses SET course_code = ?, course_name = ? WHERE course_id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, course.getCourseCode());
            pstmt.setString(2, course.getCourseName());
            pstmt.setInt(3, course.getCourseId());

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            if (e.getSQLState().equals("23505")) { // Unique violation for course_code
                System.err.println("Another course with code " + course.getCourseCode() + " already exists.");
            } else {
                e.printStackTrace();
            }
        }
        return false;
    }

    public boolean deleteCourse(int id) {
        String sql = "DELETE FROM tbl_courses WHERE course_id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean enrollUserInCourse(int userId, int courseId) {
        String sql = "INSERT INTO tbl_enrollments(user_id, course_id) VALUES(?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);
            pstmt.setInt(2, courseId);
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            if (e.getSQLState().equals("23505")) { // Unique violation (already enrolled)
                System.err.println("User " + userId + " is already enrolled in course " + courseId + ".");
            } else {
                e.printStackTrace();
            }
        }
        return false;
    }

    public List<Course> getEnrolledCoursesForUser(int userId) {
        List<Course> courses = new ArrayList<>();
        String sql = "SELECT c.course_id, c.course_code, c.course_name " +
                "FROM tbl_courses c JOIN tbl_enrollments e ON c.course_id = e.course_id " +
                "WHERE e.user_id = ? ORDER BY c.course_code";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                courses.add(new Course(
                        rs.getInt("course_id"),
                        rs.getString("course_code"),
                        rs.getString("course_name")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return courses;
    }
}