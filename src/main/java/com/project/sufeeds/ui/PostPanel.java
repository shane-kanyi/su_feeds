package com.project.sufeeds.ui;

import com.project.sufeeds.dao.CourseDao;
import com.project.sufeeds.dao.PostDao;
import com.project.sufeeds.dao.TopicDao;
import com.project.sufeeds.model.Course;
import com.project.sufeeds.model.Post;
import com.project.sufeeds.model.Topic;
import com.project.sufeeds.model.User;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

// **KEY CHANGE**: Implement both listener interfaces
public class PostPanel extends BasePanel implements CourseCreationListener, TopicCreationListener {
    private PostDao postDao;
    private TopicDao topicDao;
    private CourseDao courseDao;
    private User currentUser;

    private JComboBox<Course> courseSelectComboBox;
    private JComboBox<Topic> topicSelectComboBox;
    private JComboBox<String> postTypeComboBox;
    private JTextArea postContentArea;
    private JList<Post> postList;
    private DefaultListModel<Post> postListModel;

    public PostPanel(User user) {
        super();
        this.currentUser = user;
        this.postDao = new PostDao();
        this.topicDao = new TopicDao();
        this.courseDao = new CourseDao();
        setupPostUI();
    }

    private void setupPostUI() {
        // ... (UI setup is the same)
        JPanel contentPanel = new JPanel(new BorderLayout(10, 10));
        contentPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JPanel selectionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        selectionPanel.add(new JLabel("Course:"));
        courseSelectComboBox = new JComboBox<>();
        refreshCourseSelectionComboBox();
        courseSelectComboBox.addActionListener(e -> refreshTopicSelectionComboBox());
        selectionPanel.add(courseSelectComboBox);

        selectionPanel.add(new JLabel("Topic:"));
        topicSelectComboBox = new JComboBox<>();
        topicSelectComboBox.addActionListener(e -> refreshPostList());
        selectionPanel.add(topicSelectComboBox);

        contentPanel.add(selectionPanel, BorderLayout.NORTH);

        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        splitPane.setResizeWeight(0.5);

        JPanel addPostPanel = new JPanel(new BorderLayout(5,5));
        addPostPanel.setBorder(BorderFactory.createTitledBorder("Add New Post"));
        JPanel inputPanel = new JPanel(new GridLayout(2, 2, 5, 5));
        inputPanel.add(new JLabel("Post Type:"));
        postTypeComboBox = new JComboBox<>(new String[]{"Feedback", "Comment", "Joke", "Summary"});
        inputPanel.add(postTypeComboBox);
        inputPanel.add(new JLabel("Content:"));
        postContentArea = new JTextArea(5, 20);
        JScrollPane scrollPane = new JScrollPane(postContentArea);
        inputPanel.add(scrollPane);

        JButton addPostButton = new JButton("Add Post");
        addPostButton.addActionListener(e -> addPost());

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(addPostButton);

        addPostPanel.add(inputPanel, BorderLayout.CENTER);
        addPostPanel.add(buttonPanel, BorderLayout.SOUTH);
        splitPane.setTopComponent(addPostPanel);

        JPanel viewPostsPanel = new JPanel(new BorderLayout());
        viewPostsPanel.setBorder(BorderFactory.createTitledBorder("Posts for Selected Topic"));
        postListModel = new DefaultListModel<>();
        postList = new JList<>(postListModel);
        viewPostsPanel.add(new JScrollPane(postList), BorderLayout.CENTER);
        splitPane.setBottomComponent(viewPostsPanel);

        contentPanel.add(splitPane, BorderLayout.CENTER);
        add(contentPanel, BorderLayout.CENTER);

        refreshTopicSelectionComboBox();
    }

    private void addPost() {
        Topic selectedTopic = (Topic) topicSelectComboBox.getSelectedItem();
        if (selectedTopic == null) {
            JOptionPane.showMessageDialog(this, "Please select a topic first.", "Input Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String postType = (String) postTypeComboBox.getSelectedItem();
        String content = postContentArea.getText().trim();
        if (content.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Post content cannot be empty.", "Input Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        Post newPost = new Post(0, selectedTopic.getTopicId(), currentUser.getUserId(), postType, content, null);
        if (postDao.addPost(newPost)) {
            JOptionPane.showMessageDialog(this, "Post added successfully!");
            postContentArea.setText("");
            refreshPostList();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to add post.", "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void refreshPostList() {
        postListModel.clear();
        Topic selectedTopic = (Topic) topicSelectComboBox.getSelectedItem();
        if (selectedTopic != null) {
            List<Post> posts = postDao.getPostsByTopic(selectedTopic.getTopicId());
            for (Post post : posts) {
                postListModel.addElement(post);
            }
        }
    }

    // Public method to refresh the course dropdown
    public void refreshCourseSelectionComboBox() {
        courseSelectComboBox.removeAllItems();
        List<Course> enrolledCourses = courseDao.getEnrolledCoursesForUser(currentUser.getUserId());
        for (Course course : enrolledCourses) {
            courseSelectComboBox.addItem(course);
        }
        if (!enrolledCourses.isEmpty()) {
            courseSelectComboBox.setSelectedIndex(0);
        }
        // Important: After refreshing courses, we must also refresh the topics for the newly selected course
        refreshTopicSelectionComboBox();
    }

    // Public method to refresh the topic dropdown
    public void refreshTopicSelectionComboBox() {
        topicSelectComboBox.removeAllItems();
        Course selectedCourse = (Course) courseSelectComboBox.getSelectedItem();
        if (selectedCourse != null) {
            List<Topic> topics = topicDao.getTopicsByCourse(selectedCourse.getCourseId());
            for (Topic topic : topics) {
                topicSelectComboBox.addItem(topic);
            }
        }
        // After refreshing topics, we must refresh the posts for the newly selected topic
        refreshPostList();
    }

    // **KEY CHANGE**: Implementation for the CourseCreationListener interface
    @Override
    public void courseAdded(Course newCourse) {
        System.out.println("PostPanel detected a new course was added: " + newCourse.getCourseName());
        refreshCourseSelectionComboBox();
    }

    // **KEY CHANGE**: Implementation for the TopicCreationListener interface
    @Override
    public void topicAdded(Topic newTopic) {
        System.out.println("PostPanel detected a new topic was added: " + newTopic.getTitle());
        // We only need to refresh topics if the new topic belongs to the currently selected course
        Course selectedCourse = (Course) courseSelectComboBox.getSelectedItem();
        if (selectedCourse != null && selectedCourse.getCourseId() == newTopic.getCourseId()) {
            refreshTopicSelectionComboBox();
        }
    }
}