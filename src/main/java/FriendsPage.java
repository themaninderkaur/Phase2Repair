import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FriendsPage extends JFrame{
    JButton users;
    JButton profile;
    JButton blocked;
    JButton messages;
    JPanel panel;
    

    public FriendsPage() {
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
        profile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                profilePage();
                dispose();
            }
        });
        blocked.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //blockedPage();
                dispose();
            }
        });

        menu.add(users);
        menu.add(profile);
        menu.add(blocked);
        menu.add(messages);

        panel.add(menu, BorderLayout.NORTH);

        panel.add(new JPanel(), BorderLayout.WEST);
        panel.add(new JPanel(), BorderLayout.EAST);

        // Add panel to the frame
        add(panel);

        setVisible(true);

    }

    public void profilePage() {
        MainPage profilePage = new MainPage();
    }

    public static void main(String[] args) {
        FriendsPage main = new FriendsPage();
    }
}
