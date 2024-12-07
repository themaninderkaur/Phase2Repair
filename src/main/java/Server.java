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
                    out.write(findUsers(currentUser) + "Press any button to continue.");
                    out.println();
                    out.flush();
                    in.readLine();

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

    private User handleSignUp() {
        String correctUser;
        String correctPass;
        boolean valid = false;
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter writer = new PrintWriter(socket.getOutputStream());
            BufferedWriter filee = new BufferedWriter(new FileWriter("src\\main\\java\\userlist.txt"));

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
            User newUser = new User((long)userList.size()+1, correctUser, correctPass, email, profilePictureUrl, bio);
            userList.add(newUser);
            filee.write(userList.size() + "," + correctUser + "," + correctPass + "," + email + "," + profilePictureUrl + "," + bio);
            filee.flush();
            filee.close();

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

    private void handleFriends(User currentUser) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter writer = new PrintWriter(socket.getOutputStream());


            boolean valid = false;

            do {
                writer.write("Enter [1] to add friends, [2] to remove friends, and [3] to view friends. No brackets are necessary, just enter the number. Type [exit] to cancel.");
                writer.println();
                writer.flush();
                String result = reader.readLine();
                if (result.equals("1")) {
                    writer.write("Enter the username of the friend you wish to add.");
                    writer.println();
                    writer.flush();

                    result = reader.readLine();
                    if(!userExists(result, userList)) {
                        writer.write(result + " doesn't exist. Press any button to continue.");
                    } else if (currentUser.addFriend(result)) {
                        writer.write(result + " has been added. Press any button to continue.");
                        updateFriends();

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
                        updateFriends();
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
                    writer.write("Invalid input. Try again.");
                }
            } while (!valid);


        } catch (IOException e) {
            System.out.println("Error. Press any button to continue.");
            return;
        }
    }

    private void handleBlocked(User currentUser) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter writer = new PrintWriter(socket.getOutputStream());

            boolean valid = false;

            do {
                writer.write("Enter [1] to block a user, [2] to unblock a user, [3] to view blocked users. No brackets are necessary, just enter the number. Type [exit] to cancel.");
                writer.println();
                writer.flush();
                String result = reader.readLine();

                if (result.equals("1")) {
                    // Block a user
                    writer.write("Enter the username of the user you wish to block.");
                    writer.println();
                    writer.flush();

                    result = reader.readLine();
                    if (!userExists(result, userList)) {
                        writer.write(result + " doesn't exist. Press any button to continue.");
                    } else if (!result.equals(currentUser.getUsername())) {
                        currentUser.blockUser(result);
                        writer.write(result + " has been blocked. Press any button to continue.");
                        updateBlocked();
                    } else {
                        writer.write("You cannot block yourself. Press any button to continue.");
                    }
                    writer.println();
                    writer.flush();
                    reader.readLine();
                    return;

                } else if (result.equals("2")) {
                    // Unblock a user
                    writer.write("Enter the username of the user you wish to unblock.");
                    writer.println();
                    writer.flush();

                    result = reader.readLine();
                    if (currentUser.getBlockedList().contains(result)) {
                        currentUser.unblockUser(result);
                        writer.write(result + " has been unblocked. Press any button to continue.");
                        updateBlocked();
                    } else {
                        writer.write("This user is not in your blocked list. Press any button to continue.");
                    }
                    writer.println();
                    writer.flush();
                    reader.readLine();
                    return;

                } else if (result.equals("3")) {
                    // View blocked users
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
            System.out.println("Error handling blocked users. Press any button to continue.");
            return;
        }
    }
// Write a message to a file
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
                // Sending a message
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
                
                                if (writeMessageToFile(currentUser.getUsername(), recipientUsername, messageContent)) {
                                    writer.write("Message sent to " + recipientUsername + ". Press any button to continue.");
                                } else {
                                    writer.write("Failed to send the message. Press any button to continue.");
                                }
                                writer.println();
                                writer.flush();
                                reader.readLine();
                
                            } else if (result.equals("2")) {
                                // Viewing messages
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
                    // TODO Auto-generated method stub
                    throw new UnsupportedOperationException("Unimplemented method 'findUserByUsername'");
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

    private void updateList() {
        try {
            BufferedReader br = new BufferedReader(new FileReader("userlist.txt"));
            String line = br.readLine();

            while (line != null) {
                int userID = Integer.parseInt(line.substring(0, line.indexOf(",")));
                line = line.substring(line.indexOf(",") + 1);
                String userN = line.substring(0, line.indexOf(","));
                line = line.substring(line.indexOf(",") + 1);
                String pass = line.substring(0, line.indexOf(","));
                line = line.substring(line.indexOf(",") + 1);
                String email = line.substring(0, line.indexOf(","));
                line = line.substring(line.indexOf(",") + 1);
                String profile = line;
                line = line.substring(line.indexOf(",") + 1);
                String bio = line;

                userList.add(new User((long)userID, userN, pass, email, profile, bio));
            }

            for (User u : userList) {
                br = new BufferedReader(new FileReader("friends.txt"));
                String friendLine = br.readLine();

                while (friendLine != null) {
                    String[] friends = friendLine.split(":");

                    if (friends[0].equals(u.getUsername())) {
                        String[] userFriends = friends[1].split(",");

                        for (String userF : userFriends) {
                            u.addFriend(userF);
                        }
                    }
                }
            }

            for (User u : userList) {
                br = new BufferedReader(new FileReader("blocked.txt"));
                String blockedLine = br.readLine();

                while (blockedLine != null) {
                    String[] blockeds = blockedLine.split(":");

                    if (blockeds[0].equals(u.getUsername())) {
                        String[] userBlocked = blockeds[1].split(",");

                        for (String userB : userBlocked) {
                            u.blockUser(userB);
                        }
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Guh not working.");
        }
    }

    private void updateFriends() {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter("src\\main\\java\\friends.txt"));
            for (User u : userList) {
                String friendsList = u.getUsername() + ":";

                for (String friend : u.getFriendsList()) {
                    friendsList += ", " + friend;
                }

                bw.write(friendsList);
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Didn't wooork.");
        }

    }

    private void updateBlocked() {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter("src\\main\\java\\blocked.txt"));
            for (User u : userList) {
                String blockedList = u.getUsername() + ":";

                for (String blocked : u.getBlockedList()) {
                    blockedList += ", " + blocked;
                }

                bw.write(blockedList);
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Didn't wooork.");
        }
    }
}
