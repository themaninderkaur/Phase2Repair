package src.main.java;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;

public class Client {
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    private JFrame frame;
    private CardLayout cardLayout;
    private JPanel mainPanel;

    public Client() {
        try {
            socket = new Socket("localhost", 4343);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
            initializeGUI();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initializeGUI() {
        frame = new JFrame("Social Media App");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // Login Panel
        JPanel loginPanel = new JPanel();
        loginPanel.setLayout(new GridLayout(3, 2));
        JTextField usernameField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        JButton loginButton = new JButton("Login");
        JButton signupButton = new JButton("Go to Signup");

        loginPanel.add(new JLabel("Username:"));
        loginPanel.add(usernameField);
        loginPanel.add(new JLabel("Password:"));
        loginPanel.add(passwordField);
        loginPanel.add(loginButton);
        loginPanel.add(signupButton);

        // Signup Panel
        JPanel signupPanel = new JPanel();
        signupPanel.setLayout(new GridLayout(5, 2));
        JTextField signupUsernameField = new JTextField();
        JPasswordField signupPasswordField = new JPasswordField();
        JTextField emailField = new JTextField();
        JTextField profilePictureField = new JTextField();
        JTextField bioField = new JTextField();
        JButton signupSubmitButton = new JButton("Signup");
        JButton backToLoginButton = new JButton("Back to Login");

        signupPanel.add(new JLabel("Username:"));
        signupPanel.add(signupUsernameField);
        signupPanel.add(new JLabel("Password:"));
        signupPanel.add(signupPasswordField);
        signupPanel.add(new JLabel("Email:"));
        signupPanel.add(emailField);
        signupPanel.add(new JLabel("Profile Picture URL:"));
        signupPanel.add(profilePictureField);
        signupPanel.add(new JLabel("Bio:"));
        signupPanel.add(bioField);
        signupPanel.add(signupSubmitButton);
        signupPanel.add(backToLoginButton);

        mainPanel.add(loginPanel, "Login");
        mainPanel.add(signupPanel, "Signup");

        frame.add(mainPanel);
        frame.setVisible(true);

        // Action Listeners
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());
                out.println("login");
                out.println(username);
                out.println(password);
                handleServerResponse();
            }
        });

        signupButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(mainPanel, "Signup");
            }
        });

        signupSubmitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = signupUsernameField.getText();
                String password = new String(signupPasswordField.getPassword());
                String email = emailField.getText();
                String profilePictureUrl = profilePictureField.getText();
                String bio = bioField.getText();

                out.println("signup");
                out.println(username);
                out.println(password);
                out.println(email);
                out.println(profilePictureUrl);
                out.println(bio);
                handleServerResponse();
            }
        });

        backToLoginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(mainPanel, "Login");
            }
        });
    }

    private void handleServerResponse() {
        try {
            String response = in.readLine();
            JOptionPane.showMessageDialog(frame, response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Client::new);
    }
}
