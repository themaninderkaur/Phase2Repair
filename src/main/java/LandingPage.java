package src.main.java;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LandingPage extends JFrame {
    private Client client;

    public LandingPage(Client client) {
        this.client = client;
        setTitle("Social Media App");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 1));

        JButton loginButton = new JButton("Login");
        JButton signupButton = new JButton("Signup");

        loginButton.addActionListener(e -> {
            LoginPage loginPage = new LoginPage(client);
            loginPage.setVisible(true);
            this.dispose();
        });

        signupButton.addActionListener(e -> {
            SignupPage signupPage = new SignupPage(client);
            signupPage.setVisible(true);
            this.dispose();
        });

        panel.add(loginButton);
        panel.add(signupButton);
        add(panel);
    }
}