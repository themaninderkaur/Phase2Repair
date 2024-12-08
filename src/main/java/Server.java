import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class Server implements Runnable {
    private Socket socket;
    private ArrayList<User> userList;
    private UserInterface userint;
    private ArrayList<User> blockedUsers;
    private ArrayList<User> friends;
    private User currentUser;

    public Server(Socket socket, ArrayList<User> users) {
        this.socket = socket;
        this.userList = users;
    }

    public void run() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {
                /**
                 * ok, there's the login/signup step, and there's
                 * also the normal stuff past that.
                 */
                while (true) {
                    String command = in.readLine();

                    if (command.equals("LOGIN")) {
                        currentUser = null;
                        /**
                         * if we continuously call this every time
                         * the login button is pressed.
                         * and make sure there's a number entered
                         * if 3 times pass, then it kicks the person out
                         * back into the landing page
                         * regardless, compares it to all of the checks
                         * we can probably out.print the result of login
                         * if its SUCCESS, then switch to the SUCCESS page
                         */
                        out.println(login(in, out));
                        
                    } else if (command.equals("SIGNUP")) {
                        currentUser = null;
                        /*
                         * hi go do the signup commands
                         */
                    } else if (command.equals("MAINPAGE")) {
                        mainP(in, out);

                    } else if (command.equals("USERPAGE")) {
                        
                    } else if (command.equals("FRIENDSPAGE")) {
                        friendsP(in, out);
                    }
                }


        } catch (IOException e) {
            System.err.println("Error handling client: " + e.getMessage());
        }
    }

    private String login(BufferedReader in, PrintWriter out) {
        try {
            String userpassCombo = in.readLine();
            if (checkExists(userpassCombo) != null) {
                currentUser = checkExists(userpassCombo);
                return "SUCCESS";
            } else {
                return "FAIL";
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "FAIL";
    }

    private User checkExists(String userpassCombo) {
        for (User u : userList) {
            if ((u.getUsername() + u.getPassword()).equals(userpassCombo)) {
                return u;
            }
        }
        return null;
    }

    private void mainP(BufferedReader in, PrintWriter out) {
        while (true) {
            try {
                if (in.readLine().equals("IN MAIN")) {
                    out.println(currentUser.getBio());
                } else if (in.readLine().equals("BIO DONE")) {
                    out.println(currentUser.getUsername());
                } else if (in.readLine().equals("USER DONE")) {
                    out.println(currentUser.getEmail());
                } else if (in.readLine().equals("FRIENDSPAGE")) {
                    friendsP(in, out);
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    private void friendsP(BufferedReader in, PrintWriter out) {
        String read;
        try {
            read = in.readLine();
            if (read.contains("ADDFRIEND")) {
                read = read.substring(9);
                if (!userExists(read, userList)) {
                    out.println("NONEXISTENT");
                } else {
                    currentUser.addFriend(read);
                    out.println(currentUser.getFriendsList().toString());
                }
            } else if (read.contains("REMOVEFRIEND")) {
                read = read.substring(12);
                if (!userExists(read, userList)) {
                    out.println("NONEXISTENT");
                } else {
                    currentUser.removeFriend(read);
                    out.println(currentUser.getFriendsList().toString());
                }
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        
    }

    private boolean userExists(String username, ArrayList<User> userList) {
        for (User u : userList) {
            if (u.getUsername().equals(username)) {
                return true;
            }
        }
        return false;
    }

    public static void main(String[] args) {
        try {
            ArrayList<User> users = new ArrayList<>();
            ServerSocket serverSocket = new ServerSocket(4343);
            System.out.println("Server is running on port 4343...");
            while (true) {
                Socket clientSocket = serverSocket.accept();
                new Thread(new Server(clientSocket, users)).start();
            }
        } catch (IOException e) {
            System.err.println("Server could not start: " + e.getMessage());
        }
    }
}



