package src.main.java;

import java.io.*;
import java.net.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Client {
    private JFrame frame;
    private CardLayout cardLayout;
    private JPanel cardPanel;
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    public Client() {
        frame = new JFrame("Social Media App");
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        try {
            socket = new Socket("localhost", 4343); // Connect to the server
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Could not connect to the server.", "Connection Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1); // Exit if the connection fails
        }

        // Landing Page
        JPanel landingPanel = new JPanel();
        JButton loginButton = new JButton("Login");
        JButton signupButton = new JButton("Signup");
        landingPanel.add(loginButton);
        landingPanel.add(signupButton);
        cardPanel.add(landingPanel, "Landing");

        // Action Listeners
        loginButton.addActionListener(e -> {
            // Handle login logic here
            new LoginGUI(this);
            frame.setVisible(false); // Hide the landing page
        });

        signupButton.addActionListener(e -> {
            // Show the signup GUI
            new SignupGUI(this);
            frame.setVisible(false); // Hide the landing page
        });

        frame.add(cardPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    public void showLanding() {
        frame.setVisible(true);
    }

    // Method to handle signup
    public void signup(String username, String password, String email) {
        new Thread(() -> {
            try {
                out.println("signup"); // Send signup command to server
                out.println(username);
                out.println(password);
                out.println(email);
                String response = in.readLine(); // Read server response
                JOptionPane.showMessageDialog(frame, response);
            } catch (IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(frame, "Error during signup.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }).start();
    }

    // Method to handle login
    public void login(String username, String password) {
        new Thread(() -> {
            try {
                out.println("login"); // Send login command to server
                out.println(username);
                out.println(password);
                String response = in.readLine(); // Read server response
                JOptionPane.showMessageDialog(frame, response);
            } catch (IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(frame, "Error during login.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }).start();
    }

    // Close resources
    public void close() {
        try {
            in.close();
            out.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Client::new);
    }
}