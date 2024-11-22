package src.main.java;

import src.main.java.User;

import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

public interface UserInterface {
    Scanner scanner = new Scanner(System.in);

    default void signUp(ArrayList<User> users) {
        boolean valid = false;
        String correctUser = "";
        String correctPass = "";
        do {
            System.out
                    .println("Enter your username. Usernames must be 6-30 characters and can only be letters/numbers ");
            String username = scanner.nextLine();

            if (findUser(username, users)) {
                System.out.println("Username is already taken.");
            } else if (username.length() < 6) {
                System.out.println("Username is too short.");
            } else if (username.length() > 30) {
                System.out.println("Username is too long.");
            } else if (!username.matches("([A-Za-z0-9])*")) {
                System.out.println("Username must consist only of letters and numbers.");
            } else {
                valid = true;

            }
        } while (!valid);

        valid = false;
        do {
            System.out.println("Enter your password: ");
            String password = scanner.nextLine();

            if (password.length() < 8) {
                System.out.println("Passwords must be at least 8 characters long.");
            } else if (password.length() > 128) {
                System.out.println("Passwords must be less than 128 characters long.");
            } else {
                valid = true;
                correctPass = password;
            }
        } while (!valid);

        System.out.println("Enter your email: ");
        String email = scanner.nextLine();
        System.out.println("Enter your profile picture URL: ");
        String profilePictureUrl = scanner.nextLine();
        System.out.println("Enter your bio: ");
        String bio = scanner.nextLine();

        long userId = users.size() + 1;
        User newUser = new User(userId, correctUser, correctPass, email, profilePictureUrl, bio);
        users.add(newUser);
        System.out.println("Sign up successful! Welcome, " + correctUser);
    }

    default User logIn(ArrayList<User> users) {
        System.out.println("Enter your username: ");
        String username = scanner.nextLine();
        System.out.println("Enter your password: ");
        String password = scanner.nextLine();

        for (User user : users) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                System.out.println("Login successful! Welcome back, " + username);
                return user;
            }
        }

        System.out.println("Login failed. Please check your credentials.");
        return null;
    }

    default void viewProfile(User user) {
        if (user == null) {
            System.out.println("No user is logged in.");
            return;
        }

        System.out.println("\nUser Profile:");
        System.out.println("Username: " + user.getUsername());
        System.out.println("Email: " + user.getEmail());
        System.out.println("Profile Picture URL: " + user.getProfilePictureUrl());
        System.out.println("Bio: " + user.getBio());
        System.out.println("Account Created At: " + user.getCreatedAt());
    }

    default void addFriend(User currentUser, ArrayList<User> users) {
        if (currentUser == null) {
            System.out.println("No user is logged in.");
            return;
        }

        System.out.print("Enter the username of the friend to add: ");
        String friendUsername = scanner.nextLine();

        for (User user : users) {
            if (user.getUsername().equals(friendUsername) && !currentUser.getBlockedList().contains(friendUsername)) {
                currentUser.getFriendsList().add(friendUsername);
                System.out.println("Friend added successfully.");
                return;
            }
        }

        System.out.println("Failed to add friend. User not found or is blocked.");
    }

    static void blockUser(User currentUser, ArrayList<User> users) {
        if (currentUser == null) {
            System.out.println("No user is logged in.");
            return;
        }

        System.out.print("Enter the username of the user to block: ");
        String blockUsername = scanner.nextLine();

        for (User user : users) {
            if (user.getUsername().equals(blockUsername)) {
                currentUser.getBlockedList().add(blockUsername);
                currentUser.getFriendsList().remove(blockUsername);
                System.out.println("User blocked successfully.");
                return;
            }
        }

        System.out.println("Failed to block user. User not found.");
    }

    default void viewFriends(User currentUser) {
        if (currentUser == null) {
            System.out.println("No user is logged in.");
            return;
        }

        System.out.println("Friends List: " + currentUser.getFriendsList());
    }

    default void viewBlockedUsers(User currentUser) {
        if (currentUser == null) {
            System.out.println("No user is logged in.");
            return;
        }

        System.out.println("Blocked Users: " + currentUser.getBlockedList());
    }

    default String showUsers(ArrayList<User> users) {
        String results = "";

        for (User u : users) {
            results += "+ " + u.getUsername();
        }

        return results.substring(2);
    }

    default boolean findUser(String username, ArrayList<User> users) {
        for (User u : users) {
            if (u.getUsername() == username) {
                return true;
            }
        }
        return false;
    }

    default boolean isBlocked(User user1, User user2, ArrayList<User> users) {
        if (!users.contains(user1) || !users.contains(user2)) {
            return false;
        } else {
            if (user1.getBlockedList().contains(user2.getUsername())) {
                return true;
            } else {
                return false;
            }
        }
    }

    default boolean isFriend(User user1, User user2, ArrayList<User> users) {
        if (!users.contains(user1) || !users.contains(user2)) {
            return false;
        } else {
            if (user1.getFriendsList().contains(user2.getUsername())) {
                return true;
            } else {
                return false;
            }
        }
    }

    public void setRestricted(boolean restricted);

    public boolean isRestricted();

    default boolean sendMessage(User user1, User user2, ArrayList<ArrayList<String>> allMessages, ArrayList<User> users,
            String message) {
        if (!users.contains(user1) || !users.contains(user2)) {
            System.out.println("Check to see if user 1 exists.");
            return false;
        } else {
            ArrayList<String> newMessage = new ArrayList<String>();
            boolean dmExists = false;
            // Check if the recipient has blocked the sender
            if (isBlocked(user2, user1, users)) {
                System.out.println(
                        "Message cannot be sent. " + user1.getUsername() + " is blocked by " + user2.getUsername());
                return false;
            } else if (user2.isRestricted() && !isFriend(user1, user2, users)) {
                System.out
                        .println("Message cannot be sent. " + user2.getUsername()
                                + " only accepts messages from friends.");
                return false;
            } else {
                if (allMessages.size() == 0) {
                    newMessage.add(user1.getUsername() + " & " + user2.getUsername());
                    newMessage.add(user1.getUsername() + ": " + message);
                    allMessages.add(newMessage);
                    System.out.println(user1.getUsername() + " sent to " + user2.getUsername() + ": " + message);
                    return true;
                } else {
                    for (int i = 0; i < allMessages.size(); i++) {
                        if (allMessages.get(i).get(0).equals(user1.getUsername() + " & " + user2.getUsername())
                                || allMessages.get(i).get(0)
                                        .equals(user2.getUsername() + " & " + user1.getUsername())) {
                            allMessages.get(i).add(user1.getUsername() + ": " + message);
                            System.out
                                    .println(user1.getUsername() + " sent to " + user2.getUsername() + ": " + message);
                            return true;
                        }
                    }
                    newMessage.add(user1.getUsername() + " & " + user2.getUsername());
                    newMessage.add(user1.getUsername() + ": " + message);
                    allMessages.add(newMessage);
                    System.out.println(user1.getUsername() + " sent to " + user2.getUsername() + ": " + message);
                    return true;
                }
            }
        }
    }

    default boolean deleteMessage(User sender, User recipient, String message, ArrayList<ArrayList<String>> allMessages,
                                  ArrayList<User> users) {
        if (allMessages.size() == 0) {
            return false;
        } else if (!findUser(sender.getUsername(), users) || !findUser(recipient.getUsername(),users)) {
            System.out.println("Users dont exist.");
            return false;
        } else {
            for (int i = 0; i < allMessages.size(); i++) {
                if (allMessages.get(i).get(0).equals(sender.getUsername() + " & " + recipient.getUsername())
                        || allMessages.get(i).get(0).equals(recipient.getUsername() + " & " + sender.getUsername())) {
                    return allMessages.get(i).remove(message);
                }
            }
            return false;
        }
    }

    static void blockUser(User currentUser, String username) {
        if (currentUser  != null) {
            currentUser.blockUser(username);
            System.out.println(username + " has been blocked.");
        } else {
            System.out.println("No user is logged in.");
        }
    }


    default void unblockUser (User currentUser , String username) {
        if (currentUser  != null) {
            currentUser .unblockUser (username);
            System.out.println(username + " has been unblocked.");
        } else {
            System.out.println("No user is logged in.");
        }
    }

    default void addFriend(User currentUser , String friendUsername) {
        if (currentUser  != null) {
            currentUser .addFriend(friendUsername);
            System.out.println(friendUsername + " has been added as a friend.");
        } else {
            System.out.println("No user is logged in.");
        }
    }

    default void removeFriend(User currentUser , String friendUsername) {
        if (currentUser  != null) {
            currentUser .removeFriend(friendUsername);
            System.out.println(friendUsername + " has been removed from friends.");
        } else {
            System.out.println("No user is logged in.");
        }
    }

}
