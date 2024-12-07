import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.ArrayList;


public class UsersPage extends JFrame {
    JButton users;
    JButton profile;
    JButton friends;
    JButton blocked;
    JButton messages;
    JPanel panel;

    JTextArea existUsers;
    public UsersPage(User user, ArrayList<String> userList) {
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


        JButton users = new JButton("Users");
        JButton profile = new JButton("Profile");
        JButton friends = new JButton("Friends");
        JButton blocked = new JButton("Blocked");
        JButton messages = new JButton("Messages");

        JTextField friendName = new JTextField("Username");
        JButton add = new JButton("Add");
        JButton remove = new JButton("Remove");

        existUsers = new JTextArea();

        existUsers.setText("Existing users: \n");

        for (String u : userList) {
            if (!u.equals(user.getUsername())) {
                existUsers.append(u + "\n");
            }
        }

        users.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "You are already here!", "MESSAGING APP", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        messages.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //messagesPage();
                dispose();
            }
        });
        profile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                profilePage(user, userList);
                dispose();
            }
        });
        friends.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                friendPage(user, userList);
                dispose();
            }
        });

        blocked.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                blockedPage(user, userList);
                dispose();
            }
        });

        menu.add(users);
        menu.add(profile);
        menu.add(friends);
        menu.add(blocked);
        menu.add(messages);


        panel.add(menu, BorderLayout.NORTH);
        panel.add(existUsers, BorderLayout.CENTER);


        panel.add(new JPanel(), BorderLayout.WEST);
        panel.add(new JPanel(), BorderLayout.EAST);

        // Add panel to the frame
        add(panel);

        setVisible(true);
        
    }

    public void blockedPage(User user, ArrayList<String> userList) {
        BlockedPage bp = new BlockedPage(user, userList);
    }

    public void friendPage(User user, ArrayList<String> userList) {
        FriendsPage fp = new FriendsPage(user, userList);
    }

    public void profilePage(User user, ArrayList<String> userList) {
        MainPage mp = new MainPage(user, userList);
    }

    
}
