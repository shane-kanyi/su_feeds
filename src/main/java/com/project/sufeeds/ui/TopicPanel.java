package com.project.sufeeds.ui;

import com.project.sufeeds.dao.CourseDao;
import com.project.sufeeds.dao.TopicDao;
import com.project.sufeeds.model.Course;
import com.project.sufeeds.model.Topic;
import com.project.sufeeds.model.User;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

public class TopicPanel extends BasePanel {
    private TopicDao topicDao;
    private CourseDao courseDao; // Needed to get enrolled courses
    private User currentUser;

    private JComboBox<Course> courseSelectionComboBox;
    private JTextField weekNumberField;
    private JTextField topicTitleField;
    private JTextArea topicDescriptionArea;
    private JList<Topic> topicList;
    private DefaultListModel<Topic> topicListModel;

    public TopicPanel(User user) {
        super();
        this.currentUser = user;
        this.topicDao = new TopicDao();
        this.courseDao = new CourseDao();
        setupTopicUI();
    }

    private void setupTopicUI() {
        JPanel contentPanel = new JPanel(new BorderLayout(10, 10));
        contentPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Top Panel: Course Selection
        JPanel topSelectionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topSelectionPanel.add(new JLabel("Select Enrolled Course:"));
        courseSelectionComboBox = new JComboBox<>();
        refreshCourseSelectionComboBox();
        courseSelectionComboBox.addActionListener(e -> refreshTopicList());
        topSelectionPanel.add(courseSelectionComboBox);
        contentPanel.add(topSelectionPanel, BorderLayout.NORTH);

        // Center Panel: Add Topic and List Topics
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setResizeWeight(0.5); // Divide space equally

        // Left: Add Topic Section
        JPanel addTopicPanel = new JPanel(new BorderLayout(5,5));
        addTopicPanel.setBorder(BorderFactory.createTitledBorder("Add New Topic"));
        JPanel inputPanel = new JPanel(new GridLayout(4, 2, 5, 5));
        inputPanel.add(new JLabel("Week Number:"));
        weekNumberField = new JTextField();
        inputPanel.add(weekNumberField);
        inputPanel.add(new JLabel("Topic Title:"));
        topicTitleField = new JTextField();
        inputPanel.add(topicTitleField);
        inputPanel.add(new JLabel("Description:"));
        topicDescriptionArea = new JTextArea(5, 20);
        JScrollPane scrollPane = new JScrollPane(topicDescriptionArea);
        inputPanel.add(scrollPane);
        JButton addTopicButton = new JButton("Add Topic");
        addTopicButton.addActionListener(e -> addTopic());

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(addTopicButton);

        addTopicPanel.add(inputPanel, BorderLayout.CENTER);
        addTopicPanel.add(buttonPanel, BorderLayout.SOUTH);
        splitPane.setLeftComponent(addTopicPanel);

        // Right: List Topics Section
        JPanel listTopicPanel = new JPanel(new BorderLayout());
        listTopicPanel.setBorder(BorderFactory.createTitledBorder("Topics for Selected Course"));
        topicListModel = new DefaultListModel<>();
        topicList = new JList<>(topicListModel);
        listTopicPanel.add(new JScrollPane(topicList), BorderLayout.CENTER);
        splitPane.setRightComponent(listTopicPanel);

        contentPanel.add(splitPane, BorderLayout.CENTER);
        add(contentPanel, BorderLayout.CENTER);

        // Initial load
        refreshTopicList();
    }

    private void refreshCourseSelectionComboBox() {
        courseSelectionComboBox.removeAllItems();
        List<Course> enrolledCourses = courseDao.getEnrolledCoursesForUser(currentUser.getUserId());
        for (Course course : enrolledCourses) {
            courseSelectionComboBox.addItem(course);
        }
        if (!enrolledCourses.isEmpty()) {
            courseSelectionComboBox.setSelectedIndex(0);
        }
    }

    private void addTopic() {
        Course selectedCourse = (Course) courseSelectionComboBox.getSelectedItem();
        if (selectedCourse == null) {
            JOptionPane.showMessageDialog(this, "Please select a course first.", "Input Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String weekNumStr = weekNumberField.getText().trim();
        String title = topicTitleField.getText().trim();
        String description = topicDescriptionArea.getText().trim();

        if (weekNumStr.isEmpty() || title.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Week number and title cannot be empty.", "Input Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int weekNumber = Integer.parseInt(weekNumStr);
            Topic newTopic = new Topic(0, selectedCourse.getCourseId(), weekNumber, title, description);
            if (topicDao.addTopic(newTopic)) {
                JOptionPane.showMessageDialog(this, "Topic added successfully!");
                weekNumberField.setText("");
                topicTitleField.setText("");
                topicDescriptionArea.setText("");
                refreshTopicList();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to add topic.", "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Week Number must be a valid integer.", "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void refreshTopicList() {
        topicListModel.clear();
        Course selectedCourse = (Course) courseSelectionComboBox.getSelectedItem();
        if (selectedCourse != null) {
            List<Topic> topics = topicDao.getTopicsByCourse(selectedCourse.getCourseId());
            for (Topic topic : topics) {
                topicListModel.addElement(topic);
            }
        }
    }
}