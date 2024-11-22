package src.main.java;

import java.util.ArrayList;

public class Databases {
    private ArrayList<User> users;
    private ArrayList<ArrayList<Friends>> friends; // 2D ArrayList for friends
    private ArrayList<ArrayList<Message>> messages; // 2D ArrayList for messages
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
        // Initialize new ArrayLists for this user's friends, messages, and blocked users
        friends.add(new ArrayList<>());
        messages.add(new ArrayList<>()); // Initialize messages for this user
        blockedUsers.add(new ArrayList<>());
    }

    public void addFriend(int userIndex, Friends friends) {
        if (userIndex >= 0 && userIndex < friends.size()) {
            friends.get(userIndex).add(friends);
        } else {
            throw new IndexOutOfBoundsException("User  index out of bounds.");
        }
    }

    public void addMessage(int senderIndex, int receiverIndex, Message message) {
        if (senderIndex >= 0 && senderIndex < messages.size() && receiverIndex >= 0 && receiverIndex < messages.size()) {
            // Add the message to the sender's and receiver's message lists
            messages.get(senderIndex).add(message);
            messages.get(receiverIndex).add(message); // Optionally, you can store it in both lists
        } else {
            throw new IndexOutOfBoundsException("User  index out of bounds.");
        }
    }

    public void addBlockedUser (int userIndex, BlockedUser  blockedUser ) {
        if (userIndex >= 0 && userIndex < blockedUsers.size()) {
            blockedUsers.get(userIndex).add(blockedUser );
        } else {
            throw new IndexOutOfBoundsException("User  index out of bounds.");
        }
    }

    public void addBlockedUser (long blockerId, long blockedId) {
        BlockedUser.addBlockedUser (blockedUsers, blockerId, blockedId);
    }

    public boolean removeBlockedUser (long blockerId, long blockedId) {
        return BlockedUser.removeBlockedUser (blockedUsers, blockerId, blockedId);
    }

    // Getters for the lists
    public ArrayList<User> getUsers() {
        return users;
    }

    public ArrayList<Friends> getFriends(int userIndex) {
        if (userIndex >= 0 && userIndex < friends.size()) {
            return friends.get(userIndex);
        } else {
            throw new IndexOutOfBoundsException("User  index out of bounds.");
        }
    }

    public ArrayList<Message> getMessages(int userIndex) {
        if (userIndex >= 0 && userIndex < messages.size()) {
            return messages.get(userIndex);
        } else {
            throw new IndexOutOfBoundsException("User  index out of bounds.");
        }
    }

    public ArrayList<BlockedUser > getBlockedUsers(int userIndex) {
        if (userIndex >= 0 && userIndex < blockedUsers.size()) {
            return blockedUsers.get(userIndex);
        } else {
            throw new IndexOutOfBoundsException("User  index out of bounds.");
        }
    }
}
