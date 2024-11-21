import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

public interface UserInterface {
    Scanner scanner = new Scanner(System.in);

    default void signUp(ArrayList<User> users) {
        System.out.println("Enter your username: ");
        String username = scanner.nextLine();
        System.out.println("Enter your password: ");
        String password = scanner.nextLine();
        System.out.println("Enter your email: ");
        String email = scanner.nextLine();
        System.out.println("Enter your profile picture URL: ");
        String profilePictureUrl = scanner.nextLine();
        System.out.println("Enter your bio: ");
        String bio = scanner.nextLine();

        long userId = users.size() + 1;
        User newUser = new User(userId, username, password, email, profilePictureUrl, bio);
        users.add(newUser);
        System.out.println("Sign up successful! Welcome, " + username);
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

    default void blockUser(User currentUser, ArrayList<User> users) {
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
}
