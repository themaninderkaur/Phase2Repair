package src.main.java;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LandingPage extends JFrame {
    public LandingPage() {
        // Set up the frame
        setTitle("Social Media App");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Create a panel
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 1));

        // Create buttons
        JButton loginButton = new JButton("Login");
        JButton signupButton = new JButton("Signup");

        // Add action listeners
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openLoginPage();
            }
        });

        signupButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openSignupPage();
            }
        });

        // Add buttons to the panel
        panel.add(loginButton);
        panel.add(signupButton);

        // Add panel to the frame
        add(panel);
    }

    private void openLoginPage() {
        // Create and show the login page
        LoginPage loginPage = new LoginPage();
        loginPage.setVisible(true);
        this.dispose(); // Close the landing page
    }

    private void openSignupPage() {
        // Create and show the signup page
        SignupPage signupPage = new SignupPage();
        signupPage.setVisible(true);
        this.dispose(); // Close the landing page
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LandingPage landingPage = new LandingPage();
            landingPage.setVisible(true);
        });
    }
}