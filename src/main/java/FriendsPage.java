import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class FriendsPage extends JFrame{
    JButton users;
    JButton profile;
    JButton blocked;
    JButton messages;
    JPanel panel;

    JTextArea friendsList;
    

    public FriendsPage(BufferedReader in, PrintWriter out) {
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
        

        JPanel edit = new JPanel();
        edit.setLayout(new GridLayout(0, 3));
        edit.add(friendName);
        edit.add(add);
        edit.add(remove);

        panel.add(friendsList, BorderLayout.CENTER);

        add.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                out.println("ADDFRIEND" + friendName.getText());
                String result;
                try {
                    result = in.readLine();
                    if (result.equals("NONEXISTENT")) {
                        JOptionPane.showMessageDialog(panel, "USER DOESNT EXIST", "WARNING", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        String[] friendList = result.split(",");
                        friendsList.setText("FRIENDS LIST: \n");
                        for (String s : friendList) {
                            friendsList.append(s);
                        }
                    }
                } catch (IOException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
                
            }
        });

        remove.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                out.println("REMOVEFRIEND" + friendName.getText());
                String result;
                try {
                    result = in.readLine();
                    if (result.equals("NONEXISTENT")) {
                        JOptionPane.showMessageDialog(panel, "USER DOESNT EXIST", "WARNING", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        String[] friendList = result.split(",");
                        friendsList.setText("FRIENDS LIST: \n");
                        for (String s : friendList) {
                            friendsList.append(s);
                        }
                    }
                } catch (IOException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
                
            }

        });

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
        profile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                profilePage(in, out);
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

    public static void main(String[] args) {
    }

    public void blockedPage(BufferedReader in, PrintWriter out) {
        BlockedPage bp = new BlockedPage(in, out);
    }

    public void profilePage(BufferedReader in, PrintWriter out) {
        MainPage profilePage = new MainPage(in, out);
    }

    public void userPage(BufferedReader in, PrintWriter out) {
        UsersPage up = new UsersPage(in, out);
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
