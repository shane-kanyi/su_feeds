package com.project.sufeeds.ui;

import com.project.sufeeds.model.User;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private User currentUser;

    public MainFrame(User user) {
        this.currentUser = user;
        setTitle("SU Feeds - Welcome, " + currentUser.getUsername());
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        initComponents();
    }

    private void initComponents() {
        JTabbedPane tabbedPane = new JTabbedPane();

        CoursePanel coursePanel = new CoursePanel(currentUser);
        TopicPanel topicPanel = new TopicPanel(currentUser);
        PostPanel postPanel = new PostPanel(currentUser);

        // **KEY CHANGE**: Register the new, correct listener.
        // This tells TopicPanel and PostPanel to listen for ENROLLMENT changes from CoursePanel.
        coursePanel.addEnrollmentListener(topicPanel);
        coursePanel.addEnrollmentListener(postPanel);

        // This listener remains correct: PostPanel listens for new topics from TopicPanel.
        topicPanel.addTopicCreationListener(postPanel);

        tabbedPane.addTab("Courses", coursePanel);
        tabbedPane.addTab("Topics", topicPanel);
        tabbedPane.addTab("Posts", postPanel);

        add(tabbedPane, BorderLayout.CENTER);
    }
}