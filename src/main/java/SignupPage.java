//package src.main.java;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public class SignupPage extends JFrame {
    public SignupPage(BufferedReader in, PrintWriter out) {
        // Set up the frame
        setTitle("Signup");
        setSize(300, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Create a panel
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(6, 2));

        // Create labels and text fields
        JLabel usernameLabel = new JLabel("Username:");
        JTextField usernameField = new JTextField();
        JLabel passwordLabel = new JLabel("Password:");
        JPasswordField passwordField = new JPasswordField();
        JLabel emailLabel = new JLabel("Email:");
        JTextField emailField = new JTextField();
        JLabel bioLabel = new JLabel("Bio: ");
        JTextField bioField = new JTextField();

        // Create signup button
        JButton signupButton = new JButton("Signup");
        JButton backButton = new JButton("Back");

        // Add action listener for signup button
        signupButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Handle signup logic here
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());
                String email = emailField.getText();
                String bio = bioField.getText();
                
                if (userExists(username, in, out)) {
                    JOptionPane.showMessageDialog(null, "Username already exists.", "WARNING", JOptionPane.INFORMATION_MESSAGE);
                } else if (username.length() < 6) {
                    JOptionPane.showMessageDialog(null, "Username is too short.", "WARNING", JOptionPane.INFORMATION_MESSAGE);
                } else if (username.length() > 30) {
                    JOptionPane.showMessageDialog(null, "Username is too long.", "WARNING", JOptionPane.INFORMATION_MESSAGE);
                } else if (!username.matches("([A-Za-z0-9])*")) {
                    JOptionPane.showMessageDialog(null, "Username must be only letters/numbers", "WARNING", JOptionPane.INFORMATION_MESSAGE);
                } else if (password.length() < 8) {
                    JOptionPane.showMessageDialog(null, "Password is too short", "WARNING", JOptionPane.INFORMATION_MESSAGE);
                } else if (password.length() > 128) {
                    JOptionPane.showMessageDialog(null, "Password is too long.", "WARNING", JOptionPane.INFORMATION_MESSAGE);
                } else if (!password.matches("([A-Za-z0-9])*")) {
                    JOptionPane.showMessageDialog(null, "Password must be only letters/numbers", "WARNING", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    out.println("NEW USER," + username + "," + password + "," + email + "," + bio);
                    out.flush();
                }
            }

        });

        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
                backPressed(in, out);
            }
        });

        // Add components to the panel
        panel.add(usernameLabel);
        panel.add(usernameField);
        panel.add(passwordLabel);
        panel.add(passwordField);
        panel.add(emailLabel);
        panel.add(emailField);
        panel.add(bioLabel);
        panel.add(bioField);
        panel.add(signupButton);
        panel.add(backButton);

        // Add panel to the frame
        add(panel);
    }

    public void backPressed(BufferedReader in, PrintWriter out) {
        LandingPage lp = new LandingPage(in, out);
        lp.setVisible(true);
        out.println("BACK");
    }

    private boolean userExists(String username, BufferedReader in, PrintWriter out) {
        out.println("Check username exists" + username);
        try {
            if (in.readLine().equals("TRUE")) {
                return true;
            } else {
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
