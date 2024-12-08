import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class MainPage extends JFrame {
    JButton users;
    JButton friends;
    JButton blocked;
    JButton messages;
    JButton home;
    JPanel panel;
    

    public MainPage(BufferedReader in, PrintWriter out) {
        panel = new JPanel();
        // Set up the frame
        setTitle("MESSAGING APP");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Create a panel
        
        panel.setLayout(new BorderLayout());
        JPanel menu = new JPanel();
        menu.setLayout(new FlowLayout());

        menu.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);


        JButton home = new JButton("Profile");
        JButton users = new JButton("Users");
        JButton friends = new JButton("Friends");
        JButton blocked = new JButton("Blocked");
        JButton messages = new JButton("Messages");

        users.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                userPage(in, out);
                dispose();
            }
        });
        messages.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //messagesPage();
                dispose();
            }
        });
        friends.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                friendsPage(in, out);
                dispose();
            }
        });
        blocked.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                blockedPage(in, out);
                dispose();
            }
        });

        home.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "You are already here!", "MESSAGING APP", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        menu.add(home);
        menu.add(users);
        menu.add(friends);
        menu.add(blocked);
        menu.add(messages);

        panel.add(menu, BorderLayout.NORTH);

        JPanel profile = new JPanel();

        profile.setLayout(new BorderLayout(10,0));
        
        JTextArea description = new JTextArea(5, 20);
        JScrollPane desc = new JScrollPane(description);
        out.println("IN MAIN");
        try {
            description.setText(in.readLine());
        } catch (IOException e1) {
            JOptionPane.showMessageDialog(panel, "Description not working", "MESSAGING APP", JOptionPane.INFORMATION_MESSAGE);
            e1.printStackTrace();
        }
        out.println("BIO DONE");
        description.setLineWrap(true); // Enable line wrapping
        description.setWrapStyleWord(true); // Wrap at word boundaries

        JTextArea name = new JTextArea();
        try {
            name.setText(in.readLine());
        } catch (IOException e1) {
            JOptionPane.showMessageDialog(panel, "Name not working", "MESSAGING APP", JOptionPane.INFORMATION_MESSAGE);
            e1.printStackTrace();
        }
        out.println("USER DONE");

        JPanel west = new JPanel();

        profile.add(name, BorderLayout.NORTH);
        profile.add(desc, BorderLayout.CENTER);

        JTextArea email = new JTextArea();
        try {
            email.setText(in.readLine());
        } catch (IOException e1) {
            JOptionPane.showMessageDialog(panel, "Email not working", "MESSAGING APP", JOptionPane.INFORMATION_MESSAGE);
            e1.printStackTrace();
        }
        profile.add(email, BorderLayout.SOUTH);

        panel.add(profile, BorderLayout.CENTER);
        panel.add(west, BorderLayout.WEST);
        panel.add(new JPanel(), BorderLayout.EAST);

        // Add panel to the frame
        add(panel);

        setVisible(true);

    }

    public void friendsPage(BufferedReader in, PrintWriter out) {
        FriendsPage friendPage = new FriendsPage(in, out);
    }

    public static void main(String[] args) {
    }

    public void blockedPage(BufferedReader in, PrintWriter out) {
        BlockedPage bp = new BlockedPage(in, out);
    }

    public void userPage(BufferedReader in, PrintWriter out) {
        UsersPage up = new UsersPage(in, out);
    }
    
    

}