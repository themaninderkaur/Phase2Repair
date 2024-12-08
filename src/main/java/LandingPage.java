//package src.main.java;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class LandingPage extends JFrame {
    public LandingPage(BufferedReader in, PrintWriter out) {
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
                openLoginPage(in, out);
            }
        });

        signupButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openSignupPage(in, out);
            }
        });

        // Add buttons to the panel
        panel.add(loginButton);
        panel.add(signupButton);

        // Add panel to the frame
        add(panel);
    }

    private void openLoginPage(BufferedReader in, PrintWriter out) {
        // Create and show the login page
        LoginPage loginPage = new LoginPage(in, out);
        loginPage.setVisible(true);
        out.println("LOGIN");
        this.dispose(); // Close the landing page
    }

    private void openSignupPage(BufferedReader in, PrintWriter out) {
        // Create and show the signup page
        SignupPage signupPage = new SignupPage(in, out);
        signupPage.setVisible(true);
        out.println("SIGNUP");
        this.dispose(); // Close the landing page
    }

    public static void main(String[] args) {


        try {
            Socket socket = new Socket("localhost", 4343);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            LandingPage lp = new LandingPage(in, out);
            lp.setVisible(true);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}