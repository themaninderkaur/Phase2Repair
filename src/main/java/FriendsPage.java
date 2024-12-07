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

    JTextArea friendsList;
    

    public FriendsPage(User user) {
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

        JTextField friendName = new JTextField("Username");
        JButton add = new JButton("Add");
        JButton remove = new JButton("Remove");

        friendsList = new JTextArea();
        updateList(user);

        JPanel edit = new JPanel();
        edit.setLayout(new GridLayout(0, 3));
        edit.add(friendName);
        edit.add(add);
        edit.add(remove);

        panel.add(friendsList, BorderLayout.CENTER);

        add.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                user.getFriendsList().add(friendName.getText());
                updateList(user);
            }
        });

        remove.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                user.getFriendsList().remove(friendName.getText());
                updateList(user);
            }

        });

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
                profilePage(user);
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
        menu.add(profile);
        menu.add(blocked);
        menu.add(messages);

        panel.add(menu, BorderLayout.NORTH);



        panel.add(new JPanel(), BorderLayout.WEST);
        panel.add(new JPanel(), BorderLayout.EAST);
        panel.add(edit, BorderLayout.SOUTH);

        // Add panel to the frame
        add(panel);

        setVisible(true);

    }

    public void profilePage(User user) {
        MainPage profilePage = new MainPage(user);
    }

    public static void main(String[] args) {
    }

    private void updateList(User user) {
        friendsList.setText("FRIENDS LIST: \n");
            for (String friend : user.getFriendsList()) {
                friendsList.append(friend + "\n");
            }
    }

    public void blockedPage(User user) {
        BlockedPage bp = new BlockedPage(user);
    }


}
