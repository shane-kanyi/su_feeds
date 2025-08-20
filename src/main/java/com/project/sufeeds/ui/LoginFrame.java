package com.project.sufeeds.ui;

import com.project.sufeeds.dao.UserDao;
import com.project.sufeeds.model.User;
import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {
    private JTextField userField;
    private JPasswordField passField;
    private UserDao userDao;

    public LoginFrame() {
        userDao = new UserDao();
        setTitle("SU Feeds Login");
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the frame
        initComponents();
    }

    private void initComponents() {
        setLayout(new GridLayout(4, 2, 10, 10));

        // Username
        add(new JLabel("Username:"));
        userField = new JTextField();
        add(userField);

        // Password
        add(new JLabel("Password:"));
        passField = new JPasswordField();
        add(passField);

        // Copyright message (as per requirement)
        JLabel copyright = new JLabel("© 2025 SU Feeds", SwingConstants.RIGHT);
        copyright.setFont(new Font("Serif", Font.PLAIN, 10));
        JPanel copyrightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        copyrightPanel.add(copyright);
        add(copyrightPanel); // Add copyright to its own panel to control layout

        // Placeholder for alignment
        add(new JLabel(""));

        // Buttons
        JButton loginButton = new JButton("Login");
        JButton registerButton = new JButton("Register");
        add(loginButton);
        add(registerButton);

        // Action Listeners
        loginButton.addActionListener(e -> handleLogin());
        registerButton.addActionListener(e -> handleRegister());
    }

    private void handleLogin() {
        String username = userField.getText();
        String password = new String(passField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Username and password cannot be empty.", "Input Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        User user = userDao.login(username, password);
        if (user != null) {
            JOptionPane.showMessageDialog(this, "Login Successful! Welcome " + user.getUsername());
            // Open main application window here and dispose this one
            new MainFrame(user).setVisible(true); // <--- THIS LINE IS MODIFIED
            this.dispose(); // Close the login frame
        } else {
            JOptionPane.showMessageDialog(this, "Invalid username or password.", "Login Failed", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleRegister() {
        String username = userField.getText();
        String password = new String(passField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Username and password cannot be empty for registration.", "Input Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (userDao.register(username, password)) {
            JOptionPane.showMessageDialog(this, "Registration successful! You can now log in.");
            userField.setText(""); // Clear fields after successful registration
            passField.setText("");
        } else {
            JOptionPane.showMessageDialog(this, "Registration failed. The username might already be taken.", "Registration Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}