package com.project.sufeeds.ui;

import com.project.sufeeds.model.Topic;

/**
 * An interface for components that need to be notified when a new topic is created.
 */
@FunctionalInterface
public interface TopicCreationListener {
    void topicAdded(Topic newTopic);
}