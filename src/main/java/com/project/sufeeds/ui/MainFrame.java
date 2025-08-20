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

        // 1. Create instances of all panels
        CoursePanel coursePanel = new CoursePanel(currentUser);
        TopicPanel topicPanel = new TopicPanel(currentUser);
        PostPanel postPanel = new PostPanel(currentUser);

        // **KEY CHANGE**: Register the listeners to connect the panels
        // This tells topicPanel and postPanel to listen for events from coursePanel
        coursePanel.addCourseCreationListener(topicPanel);
        coursePanel.addCourseCreationListener(postPanel);

        // This tells postPanel to listen for events from topicPanel
        topicPanel.addTopicCreationListener(postPanel);

        // 2. Add the panels to the tabbed pane
        tabbedPane.addTab("Courses", coursePanel);
        tabbedPane.addTab("Topics", topicPanel);
        tabbedPane.addTab("Posts", postPanel);

        add(tabbedPane, BorderLayout.CENTER);
    }
}