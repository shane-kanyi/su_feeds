package com.project.sufeeds.ui;

import com.project.sufeeds.model.Course;

/**
 * An interface for components that need to be notified when a new course is created.
 */
@FunctionalInterface
public interface CourseCreationListener {
    void courseAdded(Course newCourse);
}