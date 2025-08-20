package com.project.sufeeds.model;

public class Course {
    private int courseId;
    private String courseCode;
    private String courseName;

    public Course(int courseId, String courseCode, String courseName) {
        this.courseId = courseId;
        this.courseCode = courseCode;
        this.courseName = courseName;
    }

    // Getters
    public int getCourseId() { return courseId; }
    public String getCourseCode() { return courseCode; }
    public String getCourseName() { return courseName; }

    // Setters (if needed for updates, though DAOs often handle this directly)
    public void setCourseId(int courseId) { this.courseId = courseId; }
    public void setCourseCode(String courseCode) { this.courseCode = courseCode; }
    public void setCourseName(String courseName) { this.courseName = courseName; }

    @Override
    public String toString() {
        return courseCode + " - " + courseName;
    }
}