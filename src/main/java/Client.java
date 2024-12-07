package src.main.java;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;

public class Client {
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private JTextArea messageArea; // Area to display messages from the server
    private JTextField inputField; // Field for user input

    public Client() {
        try {
            socket = new Socket("localhost", 4343);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
            showLandingPage();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showLandingPage() {
        JFrame frame = new JFrame("Social Media App");
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 1));

        JButton loginButton = new JButton("Login");
        JButton signupButton = new JButton("Signup");

        loginButton.addActionListener(e -> {
            showLoginPage();
            frame.dispose();
        });

        signupButton.addActionListener(e -> {
            showSignupPage();
            frame.dispose();
        });

        panel.add(loginButton);
        panel.add(signupButton);
        frame.add(panel);
        frame.setVisible(true);
    }

    private void showLoginPage() {
        JFrame frame = new JFrame("Login");
        frame.setSize(300, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 2));

        JLabel usernameLabel = new JLabel("Username:");
        JTextField usernameField = new JTextField();
        JLabel passwordLabel = new JLabel("Password:");
        JPasswordField passwordField = new JPasswordField();
        JButton loginButton = new JButton("Login");

        loginButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            out.println("login");
            out.println(username);
            out.println(password);
            try {
                String response = in.readLine();
                JOptionPane.showMessageDialog(frame, response);
                if (response.contains("successful")) {
                    showMainApp();
                    frame.dispose();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        panel.add(usernameLabel);
        panel.add(usernameField);
        panel.add(passwordLabel);
        panel.add(passwordField);
        panel.add(loginButton);
        frame.add(panel);
        frame.setVisible(true);
    }

    private void showSignupPage() {
        JFrame frame = new JFrame("Signup");
        frame.setSize(300, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(6, 2)); // Increased rows to accommodate the back button

        JLabel usernameLabel = new JLabel("Username:");
        JTextField usernameField = new JTextField();
        JLabel passwordLabel = new JLabel("Password:");
        JPasswordField passwordField = new JPasswordField();
        JLabel emailLabel = new JLabel("Email:");
        JTextField emailField = new JTextField();
        JButton signupButton = new JButton("Signup");
        JButton backButton = new JButton("Back"); // Create the Back button

        // Action listener for the Back button
        backButton.addActionListener(e -> {
            showLandingPage(); // Show the landing page
            frame.dispose(); // Close the signup frame
        });

        signupButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            String email = emailField.getText();
            out.println("signup");
            out.println(username);
            out.println(password);
            out.println(email);
            try {
                String response = in.readLine();
                JOptionPane.showMessageDialog(frame, response);
                if (response.contains("successful")) {
                    showMainApp();
                    frame.dispose();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        panel.add(usernameLabel);
        panel.add(usernameField);
        panel.add(passwordLabel);
        panel.add(passwordField);
        panel.add(emailLabel);
        panel.add(emailField);
        panel.add(signupButton);
        panel.add(backButton); // Add the Back button to the panel
        frame.add(panel);
        frame.setVisible(true);
    }

    private void showMainApp() {
        JFrame frame = new JFrame("Main Application");
        frame.setSize(400, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        messageArea = new JTextArea();
        messageArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(messageArea);
        panel.add(scrollPane, BorderLayout.CENTER);

        inputField = new JTextField();
        panel.add(inputField, BorderLayout.SOUTH);

        inputField.addActionListener(e -> {
            String message = inputField.getText();
            out.println(message); // Send the message to the server
            inputField.setText(""); // Clear the input field
        });

        frame.add(panel);
        frame.setVisible(true);

        // Start a thread to listen for messages from the server
        new Thread(() -> {
            String serverMessage;
            try {
                while ((serverMessage = in.readLine()) != null) {
                    messageArea.append("Server: " + serverMessage + "\n");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public static void main(String[] args) {
        new Client();
    }
}
