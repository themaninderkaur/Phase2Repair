package src.main.java;

import java.io.*;
import java.net.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class Server implements Runnable {
    private Socket socket;
    private CopyOnWriteArrayList<User> userList;

    public Server(Socket socket, CopyOnWriteArrayList<User> users) {
        this.socket = socket;
        this.userList = users;
    }

    @Override
    public void run() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {
            User currentUser = null;
            boolean exit = false;

            while (currentUser == null) {
                out.println("Welcome to the Social Media App. Type 'signup' or 'login' to begin:");
                String clientMessage = in.readLine();

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
                        } else {
                            out.println("Login failed. Please try again! Press any button to continue.");
                            in.readLine();
                        }
                    }
                }
            }

            while (!exit) {
                out.println("Welcome " + currentUser.getUsername() + ", what would you like to do? Enter 'exit' to log out.");
                String output = in.readLine();
                if ("exit".equalsIgnoreCase(output)) {
                    exit = true;
                } else {
                    out.println("Invalid input. Type 'exit' to log out.");
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
            String username, password;

            synchronized (userList) {
                do {
                    out.println("Enter your username. Usernames must be 6-30 characters and can only be letters/numbers.");
                    username = in.readLine();
                    if (userExists(username)) {
                        out.println("Username is already taken. Button to continue.");
                        in.readLine();
                        username = null;
                    } else if (username.length() < 6) {
                        out.println("Username is too short.");
                        in.readLine();
                        username = null;
                    } else if (username.length() > 30) {
                        out.println("Username is too long.");
                        in.readLine();
                        username = null;
                    } else if (!username.matches("[A-Za-z0-9]*")) {
                        out.println("Username must consist only of letters and numbers.");
                        in.readLine();
                        username = null;
                    }
                } while (username == null);

                do {
                    out.println("Enter your password. Passwords must be between 8-128 characters.");
                    password = in.readLine();
                    if (password.length() < 8) {
                        out.println("Password is too short.");
                        in.readLine();
                        password = null;
                    } else if (password.length() > 128) {
                        out.println("Password is too long.");
                        in.readLine();
                        password = null;
                    } else if (!password.matches("[A-Za-z0-9]*")) {
                        out.println("Please have your password be only letters/numbers.");
                        in.readLine();
                        password = null;
                    }
                } while (password == null);
            }

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
            String username, password;

            synchronized (userList) {
                int count = 0;

                do {
                    out.println("Enter your username:");
                    username = in.readLine();

                    out.println("Enter your password:");
                    password = in.readLine();

                    for (User user : userList) {
                        if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                            return user;
                        }
                    }

                    out.println("Incorrect user or password. Press any button to continue.");
                    in.readLine();
                    count++;
                } while (count < 3);

                out.println("Login unsuccessful. Too many attempts. Try again later. Press any button to continue.");
                in.readLine();
            }

            return null;
        } catch (IOException e) {
            System.err.println("Error during login: " + e.getMessage());
            return null;
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
