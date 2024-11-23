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
        public void addBlockedUser (User currentUser, String username) {
            if (currentUser  == null) {
                System.out.println("No user is logged in.");
                return;
            }

            int userIndex = findUserIndexById(currentUser.getUserId());
            if (userIndex >= 0) {
                // Ensure that the blockedUsers list has a sublist for this user
                if (blockedUsers.size() <= userIndex) {
                    blockedUsers.add(new ArrayList<>());
                }
                // Add the username to the blocked list
                if (!blockedUsers.get(userIndex).contains(users.get(userIndex))) {
                    blockedUsers.get(userIndex).add(users.get(userIndex));
                    System.out.println(username + " has been added to the blocked list.");
                } else {
                    System.out.println(username + " is already in the blocked list.");
                }
            } else {
                System.out.println("User  index out of bounds.");
            }
        }

        public void removeBlockedUser (User currentUser , String username) {
            if (currentUser  == null) {
                System.out.println("No user is logged in.");
                return;
            }

            int userIndex = findUserIndexById(currentUser.getUserId());
            if (userIndex >= 0 && userIndex < blockedUsers.size()) {
                ArrayList<User> userBlockedList = blockedUsers.get(userIndex);
                if (userBlockedList.remove(users.get(userIndex))) {
                    System.out.println(username + " has been removed from the blocked list.");
                } else {
                    System.out.println(username + " is not in the blocked list.");
                }
            } else {
                System.out.println("User  index out of bounds.");
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

    public ArrayList<String> readMessageContent(int userIndex) {
        ArrayList<String> messageContents = new ArrayList<>();

        try {
            // Get the list of message objects for the specific user
            ArrayList<User> userMessages = getMessages(userIndex);

            // Extract message content from each message object
            for (User messageUser : userMessages) {
                // You might need to adjust this based on how message content is stored in your User class
                String messageContent = messageUser.getPassword(); // Or whatever method retrieves the message

                if (messageContent != null && !messageContent.isEmpty()) {
                    messageContents.add(messageContent);
                }
            }
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Error reading messages: " + e.getMessage());
        }

        return messageContents;
    }

    public ArrayList<User> getBlockedUsers(int userIndex) {
        if (userIndex >= 0 && userIndex < blockedUsers.size()) {
            return blockedUsers.get(userIndex);
        } else {
            throw new IndexOutOfBoundsException("User  index out of bounds.");
        }
    }
}