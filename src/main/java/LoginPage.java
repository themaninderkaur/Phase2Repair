//package src.main.java;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class LoginPage extends JFrame {
    int loginAttempt = 0;
    public LoginPage(BufferedReader in, PrintWriter out) {
        // Set up the frame
        setTitle("Login");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Create a panel
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 2));

        // Create labels and text fields
        JLabel usernameLabel = new JLabel("Username:");
        JTextField usernameField = new JTextField();
        JLabel passwordLabel = new JLabel("Password:");
        JPasswordField passwordField = new JPasswordField();

        // Create login button
        JButton loginButton = new JButton("Login");
        JButton backButton = new JButton("Back");

        // Add action listener for login button
        loginButton.addActionListener(new ActionListener() {
            @SuppressWarnings("unchecked")
            @Override
            public void actionPerformed(ActionEvent e) {
                if (loginAttempt == 3) {
                    JOptionPane.showMessageDialog(null, "Too many login attempts made", "MESSAGING APP", JOptionPane.INFORMATION_MESSAGE);
                    backPressed(in, out);
                    out.println("BACK");
                    out.flush();
                    dispose();
                }
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());

                out.println("LOGIN," + username + password);
                out.flush();

                try {
                    String result = in.readLine();
                    if (result.contains("SUCCESS")) {
                        result = result.substring(7);
                        String[] userStuff = result.split(",");
                        Object u = (Object) userStuff[0];
                        Object uList = (Object) userStuff[1];
                        goMain((User) u, (ArrayList<String>) uList);
                        dispose();
                    } else {
                        loginAttempt++;
                    }
                } catch (IOException e1) {
                    JOptionPane.showMessageDialog(null, "Error Occured", "MESSAGING APP", JOptionPane.INFORMATION_MESSAGE);
                    e1.printStackTrace();
                }

                


                
            }
        });
        


        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                backPressed(in, out);
                dispose();
            }
        });

        // Add components to the panel
        panel.add(usernameLabel);
        panel.add(usernameField);
        panel.add(passwordLabel);
        panel.add(passwordField);
        panel.add(loginButton);
        panel.add(backButton);

        // Add panel to the frame
        add(panel);
    }

    public void backPressed(BufferedReader in, PrintWriter out) {
        LandingPage lp = new LandingPage(in, out);
        lp.setVisible(true);
    }

    public void goMain(User user, ArrayList<String> userList) {
        MainPage mp = new MainPage(user, userList);
    }

    
}
