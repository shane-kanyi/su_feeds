package com.project.sufeeds.ui;

import javax.swing.*;
import java.awt.*;

public abstract class BasePanel extends JPanel {
    protected JLabel copyrightLabel;

    public BasePanel() {
        setLayout(new BorderLayout()); // Use BorderLayout for consistent copyright placement

        copyrightLabel = new JLabel("© 2025 SU Feeds", SwingConstants.RIGHT);
        copyrightLabel.setFont(new Font("Serif", Font.PLAIN, 10));
        add(copyrightLabel, BorderLayout.SOUTH); // Place copyright at the bottom right
    }
}