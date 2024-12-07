import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainPage extends JFrame {
    JButton users;
    JButton friends;
    JButton blocked;
    JButton messages;
    JPanel panel;
    

    public MainPage(User user) {
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
        JButton friends = new JButton("Friends");
        JButton blocked = new JButton("Blocked");
        JButton messages = new JButton("Messages");

        users.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //usersPage();
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
                System.out.println("Hi");
                friendsPage(user);
                dispose();
            }
        });
        blocked.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                blockedPage(user);
                dispose();
            }
        });

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

    public void friendsPage(User user) {
        FriendsPage friendPage = new FriendsPage(user);
    }

    public static void main(String[] args) {
        User user = new User(0, "helloWorld", "hahahehehoho", "h@gmail.com", "why.jpg", "why");
        MainPage main = new MainPage(user);
    }

    public void blockedPage(User user) {
        BlockedPage bp = new BlockedPage(user);
    }
    
    

}