package src.main.java;

import java.io.*;
import java.net.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class Server implements Runnable {
    private Socket socket;
    private CopyOnWriteArrayList<User> userList;
    private CopyOnWriteArrayList<User> blockedUsers;
    private CopyOnWriteArrayList<User> friends;

    public Server(Socket socket, CopyOnWriteArrayList<User> users) {
        this.socket = socket;
        this.userList = users;
        this.blockedUsers = new CopyOnWriteArrayList<>();
        this.friends = new CopyOnWriteArrayList<>();
    }

    @Override
    public void run() {
        updateList();
        try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {
            User currentUser = null;
            String clientMessage;

            boolean exit = false;
            boolean loginSuccess = false;

            while (currentUser == null && !loginSuccess) {
                out.println("Welcome to the Social Media App. Type 'signup' or 'login' to begin:");
                clientMessage = in.readLine();

                if (clientMessage != null) {
                    if (clientMessage.equalsIgnoreCase("signup")) {
                        currentUser = handleSignUp(in, out);
                        if (currentUser != null) {
                            out.println("Signup successful! Press any button to continue.");
                            in.readLine();
                        } else {
                            out.println("Signup failed. Please try again! Press any button to continue.");
                            in.readLine();
                        }
                    } else if (clientMessage.equalsIgnoreCase("login")) {
                        currentUser = handleLogin(in, out);
                        if (currentUser != null) {
                            out.println("Login successful! Press any button to continue.");
                            in.readLine();
                            loginSuccess = true;
                        } else {
                            out.println("Login failed. Please try again! Press any button to continue.");
                            in.readLine();
                        }
                    }
                }
            }

            while (!exit) {
                out.println("Welcome " + currentUser.getUsername() + ", what would you like to do? Enter 1 to find users," +
                        " 2 for friend commands, 3 for blocked commands, and 4 for direct messages.");
                String output = in.readLine();

                switch (output) {
                    case "1":
                        out.println(findUsers(currentUser) + "Press any button to continue.");
                        in.readLine();
                        break;
                    case "2":
                        handleFriends(currentUser, in, out);
                        break;
                    case "3":
                        handleBlocked(currentUser, in, out);
                        break;
                    case "4":
                        handleMessages(currentUser, in, out);
                        break;
                    case "exit":
                        out.println("Exiting program ... Press any button to continue.");
                        exit = true;
                        break;
                    default:
                        out.println("Incorrect syntax. Please choose from the correct choices. Press any random button to continue.");
                        in.readLine();
                        break;
                }
            }
        } catch (IOException e) {
            System.err.println("Error handling client: " + e.getMessage());
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                System.err.println("Error closing socket: " + e.getMessage());
            }
        }
    }

    private User handleSignUp(BufferedReader in, PrintWriter out) {
        try {
            out.println("Enter your username (6-30 alphanumeric characters):");
            String username = in.readLine();

            synchronized (userList) {
                if (userExists(username)) {
                    out.println("Username already exists.");
                    return null;
                }
            }

            out.println("Enter your password (8-128 alphanumeric characters):");
            String password = in.readLine();

            out.println("Enter your email:");
            String email = in.readLine();

            out.println("Enter your profile picture URL:");
            String profilePictureUrl = in.readLine();

            out.println("Enter your bio:");
            String bio = in.readLine();

            User newUser = new User((long) userList.size() + 1, username, password, email, profilePictureUrl, bio);

            synchronized (userList) {
                userList.add(newUser);
            }
            return newUser;
        } catch (IOException e) {
            System.err.println("Error during signup: " + e.getMessage());
            return null;
        }
    }

    private User handleLogin(BufferedReader in, PrintWriter out) {
        try {
            out.println("Enter your username:");
            String username = in.readLine();

            out.println("Enter your password:");
            String password = in.readLine();

            synchronized (userList) {
                for (User user : userList) {
                    if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                        return user;
                    }
                }
            }
            out.println("Invalid username or password.");
        } catch (IOException e) {
            System.err.println("Error during login: " + e.getMessage());
        }
        return null;
    }

    private String findUsers(User currentUser) {
        StringBuilder result = new StringBuilder("Existing users: ");
        synchronized (userList) {
            for (User user : userList) {
                if (!user.getUsername().equals(currentUser.getUsername())) {
                    result.append(user.getUsername()).append(", ");
                }
            }
        }
        return result.toString();
    }

    private void handleFriends(User currentUser, BufferedReader in, PrintWriter out) {
        try {
            out.println("Enter [1] to add friends, [2] to remove friends, and [3] to view friends. Type [exit] to cancel.");
            String result = in.readLine();

            switch (result) {
                case "1":
                    out.println("Enter the username of the friend you wish to add.");
                    String friendToAdd = in.readLine();
                    synchronized (userList) {
                        if (!userExists(friendToAdd)) {
                            out.println(friendToAdd + " doesn't exist.");
                        } else if (currentUser.addFriend(friendToAdd)) {
                            out.println(friendToAdd + " has been added.");
                        } else {
                            out.println("Unable to add friend.");
                        }
                    }
                    break;
                case "2":
                    out.println("Enter the username of the friend you wish to remove.");
                    String friendToRemove = in.readLine();
                    if (currentUser.removeFriend(friendToRemove)) {
                        out.println(friendToRemove + " has been removed.");
                    } else {
                        out.println("Unable to remove friend.");
                    }
                    break;
                case "3":
                    out.println("Your current friends are: " + currentUser.getFriendsList().toString());
                    break;
                case "exit":
                    break;
                default:
                    out.println("Invalid input.");
                    break;
            }
        } catch (IOException e) {
            System.err.println("Error handling friends: " + e.getMessage());
        }
    }

    private void handleBlocked(User currentUser, BufferedReader in, PrintWriter out) {
        try {
            out.println("Enter [1] to block a user, [2] to unblock a user, [3] to view blocked users. Type [exit] to cancel.");
            String result = in.readLine();

            switch (result) {
                case "1":
                    out.println("Enter the username of the user you wish to block.");
                    String userToBlock = in.readLine();
                    synchronized (userList) {
                        if (!userExists(userToBlock)) {
                            out.println(userToBlock + " doesn't exist.");
                        } else if (!userToBlock.equals(currentUser.getUsername())) {
                            currentUser.blockUser(userToBlock);
                            out.println(userToBlock + " has been blocked.");
                        } else {
                            out.println("You cannot block yourself.");
                        }
                    }
                    break;
                case "2":
                    out.println("Enter the username of the user you wish to unblock.");
                    String userToUnblock = in.readLine();
                    if (currentUser.getBlockedList().contains(userToUnblock)) {
                        currentUser.unblockUser(userToUnblock);
                        out.println(userToUnblock + " has been unblocked.");
                    } else {
                        out.println("This user is not in your blocked list.");
                    }
                    break;
                case "3":
                    out.println("Your blocked users are: " + currentUser.getBlockedList().toString());
                    break;
                case "exit":
                    break;
                default:
                    out.println("Invalid input.");
                    break;
            }
        } catch (IOException e) {
            System.err.println("Error handling blocked users: " + e.getMessage());
        }
    }

    private void handleMessages(User currentUser, BufferedReader in, PrintWriter out) {
        try {
            out.println("Message feature not implemented yet.");
        } catch (Exception e) {
            System.err.println("Error handling messages: " + e.getMessage());
        }
    }

    private void updateList() {
        try (BufferedReader br = new BufferedReader(new FileReader("userlist.txt"))) {
            String line;
            synchronized (userList) {
                while ((line = br.readLine()) != null) {
                    String[] userDetails = line.split(",");
                    User user = new User(Long.parseLong(userDetails[0]), userDetails[1], userDetails[2], userDetails[3], userDetails[4], userDetails[5]);
                    userList.add(user);
                }
            }
        } catch (IOException e) {
            System.err.println("Error updating user list: " + e.getMessage());
        }
    }

    private boolean userExists(String username) {
        synchronized (userList) {
            for (User user : userList) {
                if (user.getUsername().equals(username)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static void main(String[] args) {
        try {
            CopyOnWriteArrayList<User> users = new CopyOnWriteArrayList<>();
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
