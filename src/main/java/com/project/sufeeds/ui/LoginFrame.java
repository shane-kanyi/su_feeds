package com.project.sufeeds.ui;

import com.project.sufeeds.dao.UserDao;
import com.project.sufeeds.model.User;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class LoginFrame extends JFrame {
    private JTextField userField;
    private JPasswordField passField;
    private UserDao userDao;

    public LoginFrame() {
        userDao = new UserDao();
        setTitle("SU Feeds Login");
        // We no longer set a fixed size. We'll let the components decide.
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        initComponents();
        pack(); // This makes the window fit the preferred size of its components
        setLocationRelativeTo(null); // Center the frame after packing
        setMinimumSize(getSize()); // Prevent resizing smaller than the packed size
    }

    private void initComponents() {
        // Use BorderLayout for the main frame's content pane
        setLayout(new BorderLayout(10, 10));

        // --- Center Panel for the Form (using GridBagLayout) ---
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(new EmptyBorder(20, 20, 20, 20)); // Add padding around the form
        GridBagConstraints gbc = new GridBagConstraints();

        // Configure constraints
        gbc.insets = new Insets(5, 5, 5, 5); // Padding between components
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Username Label
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.LINE_END; // Right-align labels
        formPanel.add(new JLabel("Username:"), gbc);

        // Username Text Field
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1.0; // Allow field to grow horizontally
        userField = new JTextField(15); // Set a preferred size
        formPanel.add(userField, gbc);

        // Password Label
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0; // Reset weight
        formPanel.add(new JLabel("Password:"), gbc);

        // Password Text Field
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        passField = new JPasswordField(15);
        formPanel.add(passField, gbc);

        // Copyright Label
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2; // Span both columns
        gbc.anchor = GridBagConstraints.CENTER; // Center the label
        gbc.fill = GridBagConstraints.NONE;
        JLabel copyright = new JLabel("© 2025 SU Feeds");
        copyright.setFont(new Font("Serif", Font.PLAIN, 10));
        formPanel.add(copyright, gbc);

        // --- Bottom Panel for the Buttons ---
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton loginButton = new JButton("Login");
        JButton registerButton = new JButton("Register");
        buttonPanel.add(loginButton);
        buttonPanel.add(registerButton);

        // Add panels to the frame
        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

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
            // Success! Open the main application window
            SwingUtilities.invokeLater(() -> new MainFrame(user).setVisible(true));
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