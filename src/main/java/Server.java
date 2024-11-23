package src.main.java;

import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class Server implements Runnable {
    private Socket socket;
    private ArrayList<User> userList;

    public Server(Socket socket, ArrayList<User> users) {
        this.socket = socket;
        this.userList = users;  // Shared list of users
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
                        currentUser = handleSignUp(clientMessage);
                        if (currentUser != null) {
                            out.println("Signup successful. Please log in.");
                            currentUser = null;  // Make sure the user logs in after signing up
                        } else {
                            out.println("Signup failed. Please try again.");
                        }
                    } else if (clientMessage.startsWith("login")) {
                        currentUser = handleLogin(clientMessage);
                        if (currentUser != null) {
                            out.println("Login successful. Welcome, " + currentUser.getUsername());
                        } else {
                            out.println("Login failed. Try again.");
                        }
                    }
                } else {
                    // Logic for logged-in users
                    if (clientMessage.startsWith("add friend ")) {
                        String friendUsername = clientMessage.substring("add friend ".length());
                        String response = handleAddFriend(currentUser, friendUsername);
                        out.println(response);
                    } else if (clientMessage.startsWith("block user ")) {
                        String blockUsername = clientMessage.substring("block user ".length());
                        String response = handleBlockUser(currentUser, blockUsername);
                        out.println(response);
                    } else if (clientMessage.startsWith("unblock user ")) {
                        String unblockUsername = clientMessage.substring("unblock user ".length());
                        String response = handleUnblockUser(currentUser, unblockUsername);
                        out.println(response);
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

    private User handleSignUp(String clientMessage) {
        // Extract signup details from clientMessage and create a new User
        // Simulated example - in practice, parse the message to get actual details
        long userId = userList.size() + 1;
        String username = "NewUser" + userId;
        String password = "password";  // Default password for example
        User newUser = new User(userId, username, password, "", "", "");
        userList.add(newUser);
        return newUser;
    }

    private User handleLogin(String clientMessage) {
        // Simulated login by username
        String username = clientMessage.substring("login ".length());
        for (User user : userList) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }

    private String handleAddFriend(User currentUser, String friendUsername) {
        for (User user : userList) {
            if (user.getUsername().equals(friendUsername)) {
                currentUser.addFriend(friendUsername);
                return "Friend added successfully.";
            }
        }
        return "User not found.";
    }

    private String handleBlockUser(User currentUser, String blockUsername) {
        currentUser.blockUser(blockUsername);
        return "User blocked successfully.";
    }

    private String handleUnblockUser(User currentUser, String unblockUsername) {
        currentUser.unblockUser(unblockUsername);
        return "User unblocked successfully.";
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
