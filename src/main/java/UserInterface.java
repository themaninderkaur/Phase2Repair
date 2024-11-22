import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

public class UserInterface {
    Scanner scanner = new Scanner(System.in);
    String correctUser;
    String correctPass;


    public void signUp(ArrayList<User> users) {
        boolean valid = false;
        do {
            System.out.println("Enter your username. Usernames must be 6-30 characters and can only be letters/numbers ");
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

    public User logIn(ArrayList<User> users) {
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

    public void viewProfile(User user) {
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

    public void addFriend(User currentUser, ArrayList<User> users) {
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

    public void blockUser(User currentUser, ArrayList<User> users) {
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

    public void viewFriends(User currentUser) {
        if (currentUser == null) {
            System.out.println("No user is logged in.");
            return;
        }

        System.out.println("Friends List: " + currentUser.getFriendsList());
    }

    public void viewBlockedUsers(User currentUser) {
        if (currentUser == null) {
            System.out.println("No user is logged in.");
            return;
        }

        System.out.println("Blocked Users: " + currentUser.getBlockedList());
    }

    public String showUsers(ArrayList<User> users) {
        String results = "";

        for (User u : users) {
            results += "+ " + u.getUsername();
        }

        return results.substring(2);
    } 

    public boolean findUser(String username, ArrayList<User> users) {
        for (User u : users) {
            if (u.getUsername() == username) {
                return true;
            }
        }
        return false;
    }
}
