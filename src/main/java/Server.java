package src.main.java;

import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class Server implements Runnable {
    private Socket socket;
    private ArrayList<User> userList;
    private Databases database;

    public Server(Socket socket, Databases database) {
        this.socket = socket;
        this.database = database;
        this.userList = database.getUsers(); // Assuming getUsers returns the list of users
    }

    public void run() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

            User currentUser = null;
            String clientMessage;
            
            out.println("Welcome to the Social Media App. Type 'signup' or 'login' to begin:");

            while ((clientMessage = in.readLine()) != null) {
                if (clientMessage.equalsIgnoreCase("exit")) {
                    out.println("Exiting... Goodbye!");
                    break;
                }
                if (currentUser == null) {
                    if (clientMessage.startsWith("signup")) {
                        currentUser = UserInterface.signUp(userList);
                        out.println("Signup successful. Please log in.");
                        currentUser = null;
                    } else if (clientMessage.startsWith("login")) {
                        currentUser = UserInterface.logIn(userList);
                        if (currentUser != null) {
                            out.println("Login successful. Welcome, " + currentUser.getUsername());
                        } else {
                            out.println("Login failed. Try again.");
                        }
                    }
                } else {
                    // Handle user commands such as adding friends, blocking users, etc.
                    if (clientMessage.startsWith("add friend ")) {
                        String friendUsername = clientMessage.substring(11).trim(); // extract username after "add friend "
                        boolean found = false;
                        for (User user : userList) {
                            if (user.getUsername().equals(friendUsername)) {
                                currentUser.addFriend(friendUsername);
                                out.println("Friend added successfully.");
                                found = true;
                                break;
                            }
                        }
                        if (!found) {
                            out.println("User not found.");
                        }
                    } else if (clientMessage.startsWith("block user ")) {
                        String blockUsername = clientMessage.substring(11).trim(); // extract username after "block user "
                        boolean found = false;
                        for (User user : userList) {
                            if (user.getUsername().equals(blockUsername)) {
                                currentUser.blockUser(blockUsername);
                                out.println("User blocked successfully.");
                                found = true;
                                break;
                            }
                        }
                        if (!found) {
                            out.println("User not found.");
                        }
                    } else if (clientMessage.startsWith("unblock user ")) {
                        String unblockUsername = clientMessage.substring(13).trim(); // extract username after "unblock user "
                        currentUser.unblockUser(unblockUsername);
                        out.println("User unblocked successfully.");
                    } else if (clientMessage.equals("view friends")) {
                        out.println("Friends List: " + currentUser.getFriendsList());
                    } else if (clientMessage.equals("view blocked users")) {
                        out.println("Blocked Users: " + currentUser.getBlockedList());
                    }
                }
                out.println("Awaiting command...");
            }
        } catch (IOException e) {
            System.err.println("Error handling client: " + e.getMessage());
        }
    }

    public static void main(String[] args) throws IOException {
        Databases database = new Databases(); // Ensure this is initialized properly
        ServerSocket serverSocket = new ServerSocket(4343);
        System.out.println("Social Media Server is running...");

        while (true) {
            Socket clientSocket = serverSocket.accept();
            new Thread(new Server(clientSocket, database)).start();
        }
    }
}
