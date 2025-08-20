package com.project.sufeeds.ui;

import com.project.sufeeds.dao.CourseDao;
import com.project.sufeeds.model.Course;
import com.project.sufeeds.model.User;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class CoursePanel extends BasePanel {
    private CourseDao courseDao;
    private User currentUser;

    private JTextField courseCodeField;
    private JTextField courseNameField;
    private JComboBox<Course> allCoursesComboBox;
    private DefaultListModel<Course> enrolledCoursesListModel;
    private JList<Course> enrolledCoursesList;

    // **KEY CHANGE**: Using the new, correct listener
    private final List<EnrollmentListener> enrollmentListeners = new ArrayList<>();

    public CoursePanel(User user) {
        super();
        this.currentUser = user;
        this.courseDao = new CourseDao();
        setupCourseUI();
    }

    // Method to allow other panels to subscribe to enrollment changes
    public void addEnrollmentListener(EnrollmentListener listener) {
        enrollmentListeners.add(listener);
    }

    private void setupCourseUI() {
        JPanel contentPanel = new JPanel(new BorderLayout(10, 10));
        contentPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // --- Add Course Section ---
        JPanel addCoursePanel = new JPanel(new GridLayout(3, 2, 5, 5));
        addCoursePanel.setBorder(BorderFactory.createTitledBorder("Add New Course to System"));
        addCoursePanel.add(new JLabel("Course Code:"));
        courseCodeField = new JTextField(15);
        addCoursePanel.add(courseCodeField);
        addCoursePanel.add(new JLabel("Course Name:"));
        courseNameField = new JTextField(15);
        addCoursePanel.add(courseNameField);
        addCoursePanel.add(new JLabel()); // Placeholder
        JButton addCourseButton = new JButton("Add Course");
        addCourseButton.addActionListener(e -> addCourse());
        addCoursePanel.add(addCourseButton);

        // --- Enroll in Course Section ---
        JPanel enrollPanel = new JPanel(new GridLayout(2, 2, 5, 5));
        enrollPanel.setBorder(BorderFactory.createTitledBorder("Enroll in a Course"));
        enrollPanel.add(new JLabel("Select Course:"));
        allCoursesComboBox = new JComboBox<>();
        enrollPanel.add(allCoursesComboBox);
        enrollPanel.add(new JLabel()); // Placeholder
        JButton enrollButton = new JButton("Enroll Me");
        enrollButton.addActionListener(e -> enrollInCourse());
        enrollPanel.add(enrollButton);

        // **KEY LAYOUT FIX**: Use a new panel with BoxLayout to stack the top sections
        JPanel topContainerPanel = new JPanel();
        topContainerPanel.setLayout(new BoxLayout(topContainerPanel, BoxLayout.Y_AXIS));
        topContainerPanel.add(addCoursePanel);
        topContainerPanel.add(Box.createRigidArea(new Dimension(0, 10))); // Add spacing
        topContainerPanel.add(enrollPanel);

        // --- Enrolled Courses List ---
        JPanel enrolledListPanel = new JPanel(new BorderLayout(5,5));
        enrolledListPanel.setBorder(BorderFactory.createTitledBorder("Your Enrolled Courses"));
        enrolledCoursesListModel = new DefaultListModel<>();
        enrolledCoursesList = new JList<>(enrolledCoursesListModel);
        enrolledListPanel.add(new JScrollPane(enrolledCoursesList), BorderLayout.CENTER);

        // Add panels to the main content panel
        contentPanel.add(topContainerPanel, BorderLayout.NORTH);
        contentPanel.add(enrolledListPanel, BorderLayout.CENTER);

        add(contentPanel, BorderLayout.CENTER);

        // Initial data load
        refreshAllCoursesComboBox();
        refreshEnrolledCoursesList();
    }

    private void addCourse() {
        String code = courseCodeField.getText().trim();
        String name = courseNameField.getText().trim();

        if (code.isEmpty() || name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Course code and name cannot be empty.", "Input Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Course newCourse = new Course(0, code, name);
        if (courseDao.addCourse(newCourse)) {
            JOptionPane.showMessageDialog(this, "Course '" + newCourse.getCourseName() + "' added successfully! You can now enroll in it.", "Success", JOptionPane.INFORMATION_MESSAGE);
            courseCodeField.setText("");
            courseNameField.setText("");
            refreshAllCoursesComboBox(); // Update the enrollment combobox
            // **KEY CHANGE**: We no longer notify listeners here.
        } else {
            JOptionPane.showMessageDialog(this, "Failed to add course. It might already exist.", "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void enrollInCourse() {
        Course selectedCourse = (Course) allCoursesComboBox.getSelectedItem();
        if (selectedCourse == null) {
            JOptionPane.showMessageDialog(this, "Please select a course to enroll in.", "Selection Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (courseDao.enrollUserInCourse(currentUser.getUserId(), selectedCourse.getCourseId())) {
            JOptionPane.showMessageDialog(this, "Successfully enrolled in " + selectedCourse.getCourseName() + "!", "Success", JOptionPane.INFORMATION_MESSAGE);
            refreshEnrolledCoursesList(); // Refresh this panel's list

            // **KEY CHANGE**: Notify all listeners that an enrollment change occurred
            for (EnrollmentListener listener : enrollmentListeners) {
                listener.enrollmentChanged();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Failed to enroll. You might already be enrolled in this course.", "Enrollment Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void refreshAllCoursesComboBox() {
        allCoursesComboBox.removeAllItems();
        List<Course> allCourses = courseDao.getAllCourses();
        for (Course course : allCourses) {
            allCoursesComboBox.addItem(course);
        }
    }

    private void refreshEnrolledCoursesList() {
        enrolledCoursesListModel.clear();
        List<Course> enrolledCourses = courseDao.getEnrolledCoursesForUser(currentUser.getUserId());
        for (Course course : enrolledCourses) {
            enrolledCoursesListModel.addElement(course);
        }
    }
}