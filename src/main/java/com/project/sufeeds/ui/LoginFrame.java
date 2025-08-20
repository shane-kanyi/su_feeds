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
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        initComponents();
        pack();
        setLocationRelativeTo(null);
        setMinimumSize(getSize());
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.LINE_END;
        formPanel.add(new JLabel("Username:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        userField = new JTextField(15);
        formPanel.add(userField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0;
        formPanel.add(new JLabel("Password:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        passField = new JPasswordField(15);
        formPanel.add(passField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;
        JLabel copyright = new JLabel("© 2025 SU Feeds");
        copyright.setFont(new Font("Serif", Font.PLAIN, 10));
        formPanel.add(copyright, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton loginButton = new JButton("Login");
        JButton registerButton = new JButton("Register");
        buttonPanel.add(loginButton);
        buttonPanel.add(registerButton);

        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

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
            // **KEY CHANGE IS HERE**
            // The dispose() call is now inside the invokeLater block.
            // This guarantees the MainFrame is visible before the LoginFrame is closed.
            SwingUtilities.invokeLater(() -> {
                new MainFrame(user).setVisible(true);
                this.dispose(); // This is the corrected placement
            });
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
            userField.setText("");
            passField.setText("");
        } else {
            JOptionPane.showMessageDialog(this, "Registration failed. The username might already be taken.", "Registration Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}