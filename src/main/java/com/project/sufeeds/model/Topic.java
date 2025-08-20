package com.project.sufeeds.model;

public class Topic {
    private int topicId;
    private int courseId;
    private int weekNumber;
    private String title;
    private String description;

    public Topic(int topicId, int courseId, int weekNumber, String title, String description) {
        this.topicId = topicId;
        this.courseId = courseId;
        this.weekNumber = weekNumber;
        this.title = title;
        this.description = description;
    }

    // Getters
    public int getTopicId() { return topicId; }
    public int getCourseId() { return courseId; }
    public int getWeekNumber() { return weekNumber; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }

    // Setters (if needed)
    public void setTopicId(int topicId) { this.topicId = topicId; }
    public void setCourseId(int courseId) { this.courseId = courseId; }
    public void setWeekNumber(int weekNumber) { this.weekNumber = weekNumber; }
    public void setTitle(String title) { this.title = title; }
    public void setDescription(String description) { this.description = description; }

    @Override
    public String toString() {
        return "Week " + weekNumber + ": " + title;
    }
}