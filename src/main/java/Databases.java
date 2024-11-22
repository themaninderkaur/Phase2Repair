import java.util.ArrayList;

public class Databases {
    private ArrayList<User> users;
    private ArrayList<ArrayList<Friend>> friends; // 2D ArrayList for friends
    private ArrayList<Message> messages;
    private ArrayList<ArrayList<BlockedUser >> blockedUsers; // 2D ArrayList for blocked users

    public Databases() {
        users = new ArrayList<>();
        friends = new ArrayList<>();
        messages = new ArrayList<>();
        blockedUsers = new ArrayList<>();
    }

    // Methods to add entities
    public void addUser (User user) {
        users.add(user);
        // Initialize a new ArrayList for this user's friends and blocked users
        friends.add(new ArrayList<>());
        blockedUsers.add(new ArrayList<>());
    }

    public void addFriend(int userIndex, Friend friend) {
        if (userIndex >= 0 && userIndex < friends.size()) {
            friends.get(userIndex).add(friend);
        } else {
            System.out.println("User  index out of bounds.");
        }
    }

    public void addMessage(Message message) {
        messages.add(message);
    }

    public void addBlockedUser (int userIndex, BlockedUser  blockedUser ) {
        if (userIndex >= 0 && userIndex < blockedUsers.size()) {
            blockedUsers.get(userIndex).add(blockedUser );
        } else {
            System.out.println("User  index out of bounds.");
        }
    }

    // Getters for the lists
    public ArrayList<User> getUsers() {
        return users;
    }

    public ArrayList<Friend> getFriends(int userIndex) {
        if (userIndex >= 0 && userIndex < friends.size()) {
            return friends.get(userIndex);
        } else {
            System.out.println("User  index out of bounds.");
            return null;
        }
    }

    public ArrayList<Message> getMessages() {
        return messages;
    }

    public ArrayList<BlockedUser > getBlockedUsers(int userIndex) {
        if (userIndex >= 0 && userIndex < blockedUsers.size()) {
            return blockedUsers.get(userIndex);
        } else {
            System.out.println("User  index out of bounds.");
            return null;
        }
    }
}
