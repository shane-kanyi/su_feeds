package com.project.sufeeds.ui;

/**
 * An interface for components that need to be notified when the current user's
 * course enrollment status changes.
 */
@FunctionalInterface
public interface EnrollmentListener {
    void enrollmentChanged();
}