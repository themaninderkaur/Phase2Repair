import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class MainPage extends JFrame {
    JButton users;
    JButton friends;
    JButton blocked;
    JButton messages;
    JButton home;
    JPanel panel;
    

    public MainPage(User user, ArrayList<String> userList) {
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
                userPage(user, userList);
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
                friendsPage(user, userList);
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
        description.setText(user.getBio());
        description.setLineWrap(true); // Enable line wrapping
        description.setWrapStyleWord(true); // Wrap at word boundaries

        JTextArea name = new JTextArea();
        name.setText(user.getUsername());

        JPanel west = new JPanel();

        profile.add(name, BorderLayout.NORTH);
        profile.add(desc, BorderLayout.CENTER);

        JTextArea email = new JTextArea(user.getEmail());
        profile.add(email, BorderLayout.SOUTH);

        panel.add(profile, BorderLayout.CENTER);
        panel.add(west, BorderLayout.WEST);
        panel.add(new JPanel(), BorderLayout.EAST);

        // Add panel to the frame
        add(panel);

        setVisible(true);

    }

    public void friendsPage(User user, ArrayList<String> userList) {
        FriendsPage friendPage = new FriendsPage(user, userList);
    }

    public static void main(String[] args) {
        User user = new User(0, "helloWorld", "hahahehehoho", "h@gmail.com", "why.jpg", "why");
        User user2 = new User(1, "username02", "pass02", "email02", "profile.url","hihi");

        ArrayList<String> userList = new ArrayList();
        userList.add(user.getUsername());
        userList.add(user2.getUsername());
        MainPage main = new MainPage(user, userList);
    }

    public void blockedPage(User user, ArrayList<String> userList) {
        BlockedPage bp = new BlockedPage(user, userList);
    }

    public void userPage(User user, ArrayList<String> userList) {
        UsersPage up = new UsersPage(user, userList);
    }
    
    

}