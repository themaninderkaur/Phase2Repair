//package src.main.java;

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
        this.userList = users;
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

            do {
                out.println("Welcome " + currentUser.getUsername() + ", what would you like to do? Enter 1 to find users," +
                        " 2 for friend commands, 3 for blocked commands, and 4 for direct messages.");
                String output = in.readLine();

                if (output.equals("1")) {
                    handleUsers(currentUser, in, out);

                } else if (output.equals("2")) {
                    handleFriends(currentUser);
                } else if (output.equals("3")) {
                    handleBlocked(currentUser);
                } else if (output.equals("4")) {
                    handleMessages(currentUser);
                } else if (output.equalsIgnoreCase("exit")) {
                    out.write("Exiting program ... Press any button to continue.");
                    out.println();
                    out.flush();
                    exit = true;

                } else {
                    out.write("Incorrect syntax. Please choose from the correct choices. Press any random button to continue.");
                    out.println();
                    out.flush();
                    in.readLine();
                }

            } while (!exit);
            out.close();
        } catch (IOException e) {
            System.err.println("Error handling client: " + e.getMessage());
        }
    }


    private synchronized User handleSignUp() {
        String correctUser;
        String correctPass;
        boolean valid = false;
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter writer = new PrintWriter(socket.getOutputStream());
            BufferedWriter filee = new BufferedWriter(new FileWriter("src\\main\\java\\userlist.txt", true));

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
            User newUser = new User((long) userList.size() + 1, correctUser, correctPass, email, profilePictureUrl, bio);
            userList.add(newUser);
            filee.write(userList.size() + "," + correctUser + "," + correctPass + "," + email + "," + profilePictureUrl + "," + bio);
            filee.newLine();
            filee.flush();
            filee.close();

            return newUser;

        } catch (IOException e) {
            System.out.println("IO EXCEPTION!!!");
            return null;
        }
    }
    private synchronized User handleLogin() {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter writer = new PrintWriter(socket.getOutputStream());
            int count = 0;

            do {
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
                writer.println();
                writer.flush();
                reader.readLine();
                count++;
            } while (count < 3);
            writer.write("Login unsuccessful. Too many attempts. Try again later. Press any button to continue.");
            writer.println();
            writer.flush();
            reader.readLine();
            return null;
        } catch (IOException e) {
            System.out.println("IO EXCEPTION!!!");
            return null;
        }
    }

    private synchronized void handleUsers(User currentUser, BufferedReader in, PrintWriter out) {
        boolean valid = false;
        try {
            do {
                out.write("Enter [1] to see all users, [2] to see other user information, and [3] to view own profile. Type [exit] to cancel.");
                out.println();
                out.flush();
                String result = in.readLine();
                if (result.equals("1")) {
                    out.write(findUsers(currentUser) + "Press any button to continue.");
                    out.println();
                    out.flush();
                    in.readLine();
                    return;
                } else if (result.equals("2")) {
                    out.write("Enter the name of the user you want to see.");
                    out.println();
                    out.flush();
                    result = in.readLine();
                    User foundUser = findUserByUsername(result);
                    if (foundUser == null) {
                        out.write("This user does not exist. Press any button to continue.");
                        out.println();
                        out.flush();
                        in.readLine();
                    } else {
                        out.printf("Username: %s \n Email: %s \n Bio: %s \n", foundUser.getUsername(), foundUser.getEmail(), foundUser.getBio());
                        out.flush();
                        in.readLine();
                    }
                } else if (result.equals("3")) {
                    out.printf("Username: %s \n Email: %s \n Bio: %s \n", currentUser.getUsername(), currentUser.getEmail(), currentUser.getBio());
                        out.flush();
                        in.readLine();
                } else if (result.equals("exit")) {
                    out.printf("Ok, exiting! Press any button to continue.");
                    out.flush();
                    in.readLine();
                    valid = true;
                }
                 else {
                    out.write("Invalid input. Try again.");
                }
            } while (!valid);

        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }

    private void handleFriends(User currentUser) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter writer = new PrintWriter(socket.getOutputStream());

            boolean valid = false;

            do {
                writer.write("Enter [1] to add friends, [2] to remove friends, and [3] to view friends. Type [exit] to cancel.");
                writer.println();
                writer.flush();
                String result = reader.readLine();
                if (result.equals("1")) {
                    writer.write("Enter the username of the friend you wish to add.");
                    writer.println();
                    writer.flush();

                    result = reader.readLine();
                    if (!userExists(result, userList)) {
                        writer.write(result + " doesn't exist. Press any button to continue.");
                    } else if (currentUser.addFriend(result)) {
                        writer.write(result + " has been added. Press any button to continue.");

                    } else {
                        writer.write("Not a valid user. Press any button to proceed.");
                    }
                    writer.println();
                    writer.flush();
                    reader.readLine();
                    return;
                } else if (result.equals("2")) {
                    writer.write("Enter the username of the friend you wish to remove.");
                    writer.println();
                    writer.flush();

                    result = reader.readLine();
                    if (currentUser.removeFriend(result)) {
                        writer.write(result + " has been removed. Press any button to continue.");
                    } else {
                        writer.write("Not a valid user. Press any button to proceed.");
                    }
                    writer.println();
                    writer.flush();
                    reader.readLine();
                    return;
                } else if (result.equals("3")) {
                    writer.write("Your current friends are: " + currentUser.getFriendsList().toString() + ". Press any button to continue.");
                    writer.println();
                    writer.flush();
                    reader.readLine();
                    return;
                } else {
                    writer.write("Invalid input. Try again. Press any button to continue.");
                    writer.println();
                    writer.flush();
                    reader.readLine();
                }
            } while (!valid);

        } catch (IOException e) {
            System.out.println("Error handling friends.");
        }
    }
    private void handleBlocked(User currentUser) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter writer = new PrintWriter(socket.getOutputStream());

            boolean valid = false;

            do {
                writer.write("Enter [1] to block a user, [2] to unblock a user, [3] to view blocked users. Type [exit] to cancel.");
                writer.println();
                writer.flush();
                String result = reader.readLine();

                if (result.equals("1")) {
                    writer.write("Enter the username of the user you wish to block.");
                    writer.println();
                    writer.flush();

                    result = reader.readLine();
                    if (!userExists(result, userList)) {
                        writer.write(result + " doesn't exist. Press any button to continue.");
                    } else if (!result.equals(currentUser.getUsername())) {
                        currentUser.blockUser(result);
                        writer.write(result + " has been blocked. Press any button to continue.");
                    } else {
                        writer.write("You cannot block yourself. Press any button to continue.");
                    }
                    writer.println();
                    writer.flush();
                    reader.readLine();
                    return;

                } else if (result.equals("2")) {
                    writer.write("Enter the username of the user you wish to unblock.");
                    writer.println();
                    writer.flush();

                    result = reader.readLine();
                    if (currentUser.getBlockedList().contains(result)) {
                        currentUser.unblockUser(result);
                        writer.write(result + " has been unblocked. Press any button to continue.");
                    } else {
                        writer.write("This user is not in your blocked list. Press any button to continue.");
                    }
                    writer.println();
                    writer.flush();
                    reader.readLine();
                    return;

                } else if (result.equals("3")) {
                    if (currentUser.getBlockedList().isEmpty()) {
                        writer.write("You have no blocked users. Press any button to continue.");
                    } else {
                        writer.write("Your blocked users are: " + currentUser.getBlockedList().toString() + ". Press any button to continue.");
                    }
                    writer.println();
                    writer.flush();
                    reader.readLine();
                    return;

                } else if (result.equalsIgnoreCase("exit")) {
                    return;
                } else {
                    writer.write("Invalid input. Try again.");
                    writer.println();
                    writer.flush();
                }
            } while (!valid);

        } catch (IOException e) {
            System.out.println("Error handling blocked users.");
        }
    }

    public boolean writeMessageToFile(String senderUsername, String receiverUsername, String message) {
        synchronized (this) { // Lock to ensure thread safety
    
            String filename;
            if (senderUsername.compareTo(receiverUsername) < 0) {
                filename = "chat" + senderUsername + receiverUsername + ".txt";
            } else {
                filename = "chat" + receiverUsername + senderUsername + ".txt";
            }
    
            try (PrintWriter writer = new PrintWriter(new FileWriter(filename, true))) {
                writer.println(senderUsername + ": " + message);
                return true;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
    }
    
     
    
    // Read messages from a file
    
    public ArrayList<String> readMessagesFromFile(String senderUsername, String receiverUsername) {
        synchronized (this) { // Lock to ensure thread safety
            String filename;
            if (senderUsername.compareTo(receiverUsername) < 0) {
                filename = "chat" + senderUsername + receiverUsername + ".txt";
            } else {
                filename = "chat" + receiverUsername + senderUsername + ".txt";
            }
            ArrayList<String> messages = new ArrayList<>();

            try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    messages.add(line);
                }

            } catch (FileNotFoundException e) {
                System.out.println("No previous conversation found between " + senderUsername + " and " + receiverUsername);
            } catch (IOException e) {
    
                e.printStackTrace();
            }
           return messages;
    
        }
    
    }
    
     
    
    private void handleMessages(User currentUser) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
            boolean exit = false;
            while (!exit) {
                writer.write("Enter [1] to send a message, [2] to view messages. Type [exit] to cancel.");
                writer.println();
                writer.flush();
                String result = reader.readLine();
                if (result.equals("1")) {
                    writer.write("Enter the username of the recipient:");
                    writer.println();
                    writer.flush();
    
                    String recipientUsername = reader.readLine();
                    User recipient = findUserByUsername(recipientUsername);
    
                    if (recipient == null) {
                        writer.write("User " + recipientUsername + " does not exist. Press any button to continue.");
                        writer.println();
                        writer.flush();
                        reader.readLine();
                        continue;
                    } 
                    writer.write("Enter your message:");
                    writer.println();
                    writer.flush();
    
                    String messageContent = reader.readLine();

                    if (recipient.getBlockedList().contains(currentUser.getUsername()) || 
                    currentUser.getBlockedList().contains(recipient.getUsername())) {
                        writer.write("Cannot message user. Press any button to continue.");
                        writer.println();
                        writer.flush();
                        reader.readLine();
                        continue;
                    } else if (writeMessageToFile(currentUser.getUsername(), recipientUsername, messageContent)) {
                        writer.write("Message sent to " + recipientUsername + ". Press any button to continue.");
                    } else {
                        writer.write("Failed to send the message. Press any button to continue.");
                    }
    
                    writer.println();
                    writer.flush();
                    reader.readLine();
    
                    } else if (result.equals("2")) {
                        writer.write("Enter the username of the user whose messages you want to view:");
                        writer.println();
                        writer.flush();
                        String otherUsername = reader.readLine();
                        User otherUser = findUserByUsername(otherUsername);
    
                        if (otherUser == null) {
                            writer.write("User " + otherUsername + " does not exist. Press any button to continue.");
                            writer.println();
                            writer.flush();
                            reader.readLine();
                            continue;
                        } 
                        ArrayList<String> messages = readMessagesFromFile(currentUser.getUsername(), otherUsername);
                        writer.write("Messages with " + otherUsername + ":\n");
    
                        for (String message : messages) {
                            writer.write(message + "\n");
                        }
    
                        if (messages.isEmpty()) {
                            writer.write("No previous messages found.\n");
                        }
                        writer.write("Press any button to continue.");
                        writer.println();
                        writer.flush();
                        reader.readLine();
    
                    } else if (result.equalsIgnoreCase("exit")) {
                        writer.write("Exiting direct messages. Returning to the main menu...");
                        writer.println();
                        writer.flush();
                        exit = true;
                    } else {
                        writer.write("Invalid input. Try again.");
                        writer.println();
                        writer.flush();
                    }
                }
            } catch (IOException e) {
            System.out.println("Error handling messages: " + e.getMessage());
        }                         
    }
    
                   
    
                    
    
    private User findUserByUsername(String recipientUsername) {
        for (User u : userList) {
            if (u.getUsername().equals(recipientUsername)) {
                return u;
            }
        }
        return null;
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

    private int findUserIndexByUsername(String username) {
        for (int i = 0; i < userList.size(); i++) {
            if (userList.get(i).getUsername().equals(username)) {
                return i;
            }
        }
        return -1;
    }

    private String findUsers(User currentUser) {
        String result = "Existing users are: ";
        for (User u : userList) {
            result += u.getUsername() + ", ";
        }
        return result;
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
}



