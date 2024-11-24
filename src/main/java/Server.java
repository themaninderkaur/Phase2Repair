package src.main.java;

import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class Server implements Runnable {
    private Socket socket;
    private ArrayList<User> userList;
    private UserInterface userint;
    private ArrayList<User> blockedUsers;
    private ArrayList<User> friends;
    

    public Server(Socket socket, ArrayList<User> users) {
        this.socket = socket;
        this.userList = users;  // Shared list of users
    }

    public void run() {
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
                        currentUser = handleSignUp();
                        if (currentUser != null) {
                            out.write("Signup successful! Press any button to continue.");
                            out.println();
                            out.flush();
                            in.readLine();
                            currentUser = null;
                        } else {
                            out.write("Signup failed. Please try again!");
                            out.println();
                            out.flush();
                            in.readLine();
                        }
                    } else if (clientMessage.equalsIgnoreCase("login")) {
                        currentUser = handleLogin();
                        if (currentUser != null) {
                            out.write("Login successful! Press any button to continue.");
                            out.println();
                            out.flush();
                            in.readLine();
                            loginSuccess = true;
                            currentUser = null;
                            manageUserCommands(currentUser);
                        } else {
                            out.write("Login failed. Please try again! Press any button to continue.");
                            out.println();
                            out.flush();
                            in.readLine();
                        }
                    }
                }
            }

            System.out.println("Got here!!");

            /**while ((clientMessage = in.readLine()) != null) {
                if (clientMessage.equalsIgnoreCase("exit")) {
                    out.println("Exiting... Goodbye!");
                    break;
                }
                if (currentUser == null) {
                    if (clientMessage.startsWith("signup")) {
                        currentUser = handleSignUp();
                        if (currentUser != null) {
                            out.println("Signup successful. Please log in.");
                            currentUser = null;  // Make sure the user logs in after signing up
                        } else {
                            out.println("Signup failed. Please try again.");
                        }
                    } else if (clientMessage.startsWith("login")) {
                        currentUser = handleLogin();
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
            } */
        } catch (IOException e) {
            System.err.println("Error handling client: " + e.getMessage());
        }
    }

    private User handleSignUp() {
        String correctUser;
        String correctPass;
        boolean valid = false;
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter writer = new PrintWriter(socket.getOutputStream());

            do {
                writer.write("Enter your username. Usernames must be 6-30 characters and can only be letters/numbers.");
                writer.println();
                writer.flush();

                correctUser = reader.readLine();
                if (userExists(correctUser, userList)) {
                    writer.write("Username is already taken. Button to continue.");
                    writer.println();
                    writer.flush();
                    reader.readLine();
                } else if (correctUser.length() < 6) {
                    writer.write("Username is too short.");
                    writer.println();
                    writer.flush();
                    reader.readLine();
                } else if (correctUser.length() > 30) {
                    writer.write("Username is too long.");
                    writer.println();
                    writer.flush();
                    reader.readLine();
                } else if (!correctUser.matches("([A-Za-z0-9])*")) {
                    writer.write("Username must consist only of letters and numbers.");
                    writer.println();
                    writer.flush();
                    reader.readLine();
                } else {
                    valid = true;
                }
            } while (!valid);
            valid = false;

            do {
                writer.write("Enter your password. Passwords must be between 8-128 characters.");
                writer.println();
                writer.flush();

                correctPass = reader.readLine();
                if (correctPass.length() < 8) {
                    writer.write("Password is too short.");
                    writer.println();
                    writer.flush();
                    reader.readLine();
                } else if (correctPass.length() > 128) {
                    writer.write("Password is too long.");
                    writer.println();
                    writer.flush();
                    reader.readLine();
                } else if (!correctPass.matches("([A-Za-z0-9])*")) {
                    writer.write("Please have your password be only letters/numbers.");
                    writer.println();
                    writer.flush();
                    reader.readLine();
                } else {
                    valid = true;
                }
            } while (!valid);

            writer.write("Enter your email: ");
            writer.println();
            writer.flush();

            String email = reader.readLine();

            writer.write("Enter your profile picture URL: ");
            writer.println();
            writer.flush();
            String profilePictureUrl = reader.readLine();

            writer.write("Enter your bio: ");
            writer.println();
            writer.flush();

            String bio = reader.readLine();
            System.out.println("Got here");

            User newUser = new User((long)userList.size()+1, correctUser, correctPass, email, profilePictureUrl, bio);
            userList.add(newUser);
            return newUser;

        } catch (IOException e) {
            System.out.println("IO EXCEPTION!!!");
            return null;
        }        
    }

    private User handleLogin() {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter writer = new PrintWriter(socket.getOutputStream());
            int count = 0;

            do {
                for (User u : userList) {
                    System.out.println("Current users:" + u.getUsername() + u.getPassword());
                }
                writer.write("Enter your username: ");
                writer.println();
                writer.flush();
                String user = reader.readLine();
                
                writer.write("Enter your password: ");
                writer.println();
                writer.flush();
                String password = reader.readLine();

                for (User u : userList) {
                    if (u.getUsername().equals(user) && u.getPassword().equals(password)) {
                        return u;
                    }
                }
                writer.write("Incorrect user or password. Press any button to continue.");
                reader.readLine();
                writer.println();
                writer.flush();
                count++;
            } while (count < 3);
            writer.write("Login unsuccessful. Too many attempts. Try again later. Press any button to continue.");
            reader.readLine();
            writer.println();
            writer.flush();
            return null;
        } catch (IOException e) {
            System.out.println("IO EXCEPTION!!!");
            return null;
        }
        
    }
    private void manageUserCommands(User currentUser) throws IOException {
        String command;
        try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {
                out.println("Logged in as " + currentUser.getUsername() + ". Enter commands to manage your profile or 'logout' to exit.");
        
                while (!(command = in.readLine()).equalsIgnoreCase("logout")) {
                    switch (command.split(" ")[0]) {
                        case "add":
                            if (command.startsWith("add friend ")) {
                                String friendUsername = command.substring(11);
                                currentUser.addFriend(friendUsername); // Assuming addFriend modifies internal state
                                out.println("Friend added successfully.");
                            }
                            break;
                        case "block":
                            if (command.startsWith("block user ")) {
                                String blockUsername = command.substring(11);
                                currentUser.blockUser(blockUsername);
                                out.println("User blocked successfully.");
                            }   
                            break;
                        case "unblock":
                            if (command.startsWith("unblock user ")) {
                                String unblockUsername = command.substring(13);
                                currentUser.unblockUser(unblockUsername);
                                out.println("User unblocked successfully.");
                            }
                            break;
                        case "view":
                            if (command.equals("view friends")) {
                                out.println("Friends List: " + String.join(", ", currentUser.getFriendsList()));
                            } else if (command.equals("view blocked users")) {
                                out.println("Blocked Users: " + String.join(", ", currentUser.getBlockedList()));
                            }
                            break;
                        default:
                            out.println("Unknown command.");
                    }
                }
        }
        catch (IOException e) {
            System.err.println("Error handling client: " + e.getMessage());
        }
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

    private boolean userExists(String username, ArrayList<User> array) {
        if (array.size() < 1) {
            return false;
        } else {
            for (User u : array) {
                if (u.getUsername().equals(username)) {
                    return true;
                }
            }
            return false;
        }
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
