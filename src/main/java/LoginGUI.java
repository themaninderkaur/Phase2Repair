package src.main.java;

import javax.swing.*;
import java.awt.*;

public class LoginGUI {
    private JFrame frame;
    private Client socialMediaApp; // Reference to the main application

    public LoginGUI(Client socialMediaApp) {
        this.socialMediaApp = socialMediaApp; // Store the reference to the main application
        frame = new JFrame("Login");
        frame.setLayout(new GridLayout(3, 2)); // Simple grid layout

        // Username Field
        JLabel usernameLabel = new JLabel("Username:");
        JTextField usernameField = new JTextField(15);

        // Password Field
        JLabel passwordLabel = new JLabel("Password:");
        JPasswordField passwordField = new JPasswordField(15);

        // Login Button
        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            if (!username.isEmpty() && !password.isEmpty()) {
                socialMediaApp.login(username, password); // Call the login method in Client
                frame.dispose(); // Close the login window
            } else {
                JOptionPane.showMessageDialog(frame, "Please enter both username and password.", "Input Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Add components to the frame
        frame.add(usernameLabel);
        frame.add(usernameField);
        frame.add(passwordLabel);
        frame.add(passwordField);
        frame.add(loginButton);

        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        // Create an instance of the main application to pass to LoginGUI
        SwingUtilities.invokeLater(() -> {
            Client client = new Client(); // Create the main application
            new LoginGUI(client); // Open the login GUI
        });
    }
}