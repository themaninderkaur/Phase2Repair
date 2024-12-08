package src.main.java;

import javax.swing.*;
import java.awt.*;

public class SignupGUI {
    private JFrame frame;
    private CardLayout cardLayout;
    private JPanel cardPanel;
    private Client socialMediaApp; // Reference to the main application

    public SignupGUI(Client socialMediaApp) {
        this.socialMediaApp = socialMediaApp; // Store the reference to the main application
        frame = new JFrame("Signup");
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        // Step 1: Username
        JPanel usernamePanel = new JPanel();
        JTextField usernameField = new JTextField(15);
        JButton nextButton1 = new JButton("Next");
        usernamePanel.add(new JLabel("Username:"));
        usernamePanel.add(usernameField);
        usernamePanel.add(nextButton1);
        cardPanel.add(usernamePanel, "Username");

        // Step 2: Password
        JPanel passwordPanel = new JPanel();
        JPasswordField passwordField = new JPasswordField(15);
        JButton nextButton2 = new JButton("Next");
        passwordPanel.add(new JLabel("Password:"));
        passwordPanel.add(passwordField);
        passwordPanel.add(nextButton2);
        cardPanel.add(passwordPanel, "Password");

        // Step 3: Email
        JPanel emailPanel = new JPanel();
        JTextField emailField = new JTextField(15);
        JButton nextButton3 = new JButton("Next");
        emailPanel.add(new JLabel("Email:"));
        emailPanel.add(emailField);
        emailPanel.add(nextButton3);
        cardPanel.add(emailPanel, "Email");

        // Step 4: Profile Picture URL
        JPanel profilePicPanel = new JPanel();
        JTextField profilePicField = new JTextField(15);
        JButton nextButton4 = new JButton("Next");
        profilePicPanel.add(new JLabel("Profile Picture URL:"));
        profilePicPanel.add(profilePicField);
        profilePicPanel.add(nextButton4);
        cardPanel.add(profilePicPanel, "Profile Picture");

        // Step 5: Bio
        JPanel bioPanel = new JPanel();
        JTextArea bioArea = new JTextArea(3, 15);
        JButton submitButton = new JButton("Submit");
        bioPanel.add(new JLabel("Bio:"));
        bioPanel.add(bioArea);
        bioPanel.add(submitButton);
        cardPanel.add(bioPanel, "Bio");

        // Action Listeners
        nextButton1.addActionListener(e -> cardLayout.show(cardPanel, "Password"));
        nextButton2.addActionListener(e -> cardLayout.show(cardPanel, "Email"));
        nextButton3.addActionListener(e -> cardLayout.show(cardPanel, "Profile Picture"));
        nextButton4.addActionListener(e -> cardLayout.show(cardPanel, "Bio"));

        submitButton.addActionListener(e -> {
            // Handle final submission
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            String email = emailField.getText();
            String profilePic = profilePicField.getText();
            String bio = bioArea.getText();
            // Here you would typically send this data to the server for processing
            JOptionPane.showMessageDialog(frame, "Signup Successful!");
            frame.dispose(); // Close the signup window
            socialMediaApp.showLanding(); // Show the landing page again
        });

        frame.add(cardPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        // Create an instance of the main application to pass to SignupGUI
        SwingUtilities.invokeLater(() -> new Client());
    }
}
