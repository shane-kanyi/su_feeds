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
        setLocationRelativeTo(null); // Center the frame
        initComponents();
    }

    private void initComponents() {
        JTabbedPane tabbedPane = new JTabbedPane();

        // Panel for Course Management (Add/Enroll)
        CoursePanel coursePanel = new CoursePanel(currentUser);
        tabbedPane.addTab("Courses", coursePanel);

        // Panel for Topic Management (Add/View)
        TopicPanel topicPanel = new TopicPanel(currentUser);
        tabbedPane.addTab("Topics", topicPanel);

        // Panel for Post Management (Add/View)
        PostPanel postPanel = new PostPanel(currentUser);
        tabbedPane.addTab("Posts", postPanel);

        add(tabbedPane, BorderLayout.CENTER); // Add the tabbed pane to the frame

        // Add a general copyright label at the bottom of the MainFrame
        // (Individual panels also have them, but this is a top-level one if desired)
        JLabel mainCopyright = new JLabel("© 2025 SU Feeds - Developed by " + currentUser.getUsername(), SwingConstants.RIGHT);
        mainCopyright.setFont(new Font("Serif", Font.PLAIN, 9));
        // You might consider placing this in a separate status bar or info panel.
        // For simplicity, commenting out to avoid double copyright in this setup.
        // add(mainCopyright, BorderLayout.SOUTH);
    }
}