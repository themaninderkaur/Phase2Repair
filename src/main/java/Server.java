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
                        synchronized (this) {
                            currentUser = handleSignUp(in, out);
                        }
                        if (currentUser != null) {
                            out.println("Signup successful! Press any button to continue.");
                            in.readLine();
                        } else {
                            out.println("Signup failed. Please try again! Press any button to continue.");
                            in.readLine();
                        }
                    } else if (clientMessage.equalsIgnoreCase("login")) {
                        synchronized (this) {
                            currentUser = handleLogin(in, out);
                        }
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

                synchronized (this) {
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

    private synchronized User handleSignUp(BufferedReader in, PrintWriter out) {
        try {
            out.println("Enter your username (6-30 alphanumeric characters):");
            String username = in.readLine();

            if (userExists(username)) {
                out.println("Username already exists.");
                return null;
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
            userList.add(newUser);
            return newUser;
        } catch (IOException e) {
            System.err.println("Error during signup: " + e.getMessage());
            return null;
        }
    }

    private synchronized User handleLogin(BufferedReader in, PrintWriter out) {
        try {
            out.println("Enter your username:");
            String username = in.readLine();

            out.println("Enter your password:");
            String password = in.readLine();

            for (User user : userList) {
                if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                    return user;
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
        for (User user : userList) {
            if (!user.getUsername().equals(currentUser.getUsername())) {
                result.append(user.getUsername()).append(", ");
            }
        }
        return result.toString();
    }

    private boolean userExists(String username) {
        for (User user : userList) {
            if (user.getUsername().equals(username)) {
                return true;
            }
        }
        return false;
    }

    private void handleFriends(User currentUser, BufferedReader in, PrintWriter out) {
    }

    private void handleBlocked(User currentUser, BufferedReader in, PrintWriter out) {
    }

    private void handleMessages(User currentUser, BufferedReader in, PrintWriter out) {
    }

    private void updateList() {
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
