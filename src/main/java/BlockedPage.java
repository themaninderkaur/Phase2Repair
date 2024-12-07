import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class BlockedPage extends JFrame{
    JButton users;
    JButton profile;
    JButton friends;
    JButton blocked;
    JButton messages;
    JPanel panel;

    JTextArea blockedList;
    

    public BlockedPage(User user, ArrayList<String> userList) {
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

        blockedList = new JTextArea();
        updateList(user);

        JPanel edit = new JPanel();
        edit.setLayout(new GridLayout(0, 3));
        edit.add(friendName);
        edit.add(add);
        edit.add(remove);

        panel.add(blockedList, BorderLayout.CENTER);

        add.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (userExists(friendName.getText(), userList)) {
                    if (user.getBlockedList().add(friendName.getText())) {
                        updateList(user);
                    } else {
                        JOptionPane.showMessageDialog(null, friendName.getText() + " was already blocked!", "WARNING", JOptionPane.INFORMATION_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "No such user exists!", "WARNING", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });

        remove.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (userExists(friendName.getText(), userList)) {
                    if (user.getBlockedList().remove(friendName.getText())) {
                        updateList(user);
                    } else {
                        JOptionPane.showMessageDialog(null, friendName.getText() + " was not a friend!", "WARNING", JOptionPane.INFORMATION_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "No such user exists!", "WARNING", JOptionPane.INFORMATION_MESSAGE);
                }
                
            }

        });

        users.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                usersPage(user, userList);
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
                JOptionPane.showMessageDialog(null, "You are already here!", "MESSAGING APP", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        menu.add(users);
        menu.add(profile);
        menu.add(friends);
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

    public void profilePage(User user, ArrayList<String> userList) {
        MainPage profilePage = new MainPage(user, userList);
    }

    public void friendPage(User user, ArrayList<String> userList) {
        FriendsPage friendPage = new FriendsPage(user, userList);
    }

    public void usersPage(User user, ArrayList<String> userList) {
        UsersPage up = new UsersPage(user, userList);
    }

    public static void main(String[] args) {
    }

    private void updateList(User user) {
        blockedList.setText("BLOCKED LIST: \n");
            for (String blocked : user.getBlockedList()) {
                blockedList.append(blocked + "\n");
            }
    }

    private boolean userExists(String username, ArrayList<String> userList) {
        for (String user : userList) {
            if (user.equals(username)) {
                return true;
            }
        }
        return false;
    }


}
