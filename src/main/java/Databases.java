package src.main.java;

import java.util.ArrayList;

public class Databases {
    private ArrayList<User> users;
    private ArrayList<ArrayList<User>> friends; // 2D ArrayList for friends
    private ArrayList<ArrayList<User>> messages; // 2D ArrayList for messages
    private ArrayList<ArrayList<User>> blockedUsers; // 2D ArrayList for blocked users

    public Databases() {
        users = new ArrayList<>();
        friends = new ArrayList<>();
        messages = new ArrayList<>();
        blockedUsers = new ArrayList<>();
    }

    // Existing methods...

    public boolean addBlockedUser (long blockerId, long blockedId) {
        return User.blockUser(users.get((int)blockerId), blockedId+"");
    }

    public boolean removeBlockedUser (long blockerId, long blockedId) {
        return User.unblockUser(blockedUsers, blockerId, blockedId);
    }

    public void addFriend(long userId, long friendId) {
        // Assuming you have a way to find the user index by userId
        int userIndex = findUserIndexById(userId);
        int friendIndex = findUserIndexById(friendId);
        if (userIndex >= 0 && friendIndex >= 0) {
            User newFriend = new User(userId, friendId+"", "accepted", null, null,null);
            friends.get(userIndex).add(newFriend);
            users.get(userIndex).addFriend(users.get(friendIndex).getUsername());
        }
    }

    public void addMessage(long senderId, long receiverId, String content, String photoUrl, String bio) {
        // Assuming you have a way to find the user index by userId
        int senderIndex = findUserIndexById(senderId);
        int receiverIndex = findUserIndexById(receiverId);
        if (senderIndex >= 0 && receiverIndex >= 0) {
            User newMessage = new User(messages.size() + 1, senderId +"", receiverId + "", content, photoUrl, bio);
            messages.get(senderIndex).add(newMessage);
            messages.get(receiverIndex).add(newMessage); // Optionally, store it in both lists
        }
    }

    private int findUserIndexById(long userId) {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getUserId() == userId) {
                return i;
            }
        }
        return -1; // User not found
    }

    // Getters for the lists
    public ArrayList<User> getUsers() {
        return users;
    }

    public ArrayList<User> getFriends(int userIndex) {
        if (userIndex >= 0 && userIndex < friends.size()) {
            return friends.get(userIndex);
        } else {
            throw new IndexOutOfBoundsException("User  index out of bounds.");
        }
    }

    public ArrayList<User> getMessages(int userIndex) {
        if (userIndex >= 0 && userIndex < messages.size()) {
            return messages.get(userIndex);
        } else {
            throw new IndexOutOfBoundsException("User  index out of bounds.");
        }
    }

    public ArrayList<User> getBlockedUsers(int userIndex) {
        if (userIndex >= 0 && userIndex < blockedUsers.size()) {
            return blockedUsers.get(userIndex);
        } else {
            throw new IndexOutOfBoundsException("User  index out of bounds.");
        }
    }
}