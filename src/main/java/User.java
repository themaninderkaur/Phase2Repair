//package src.main.java;

import java.util.Date;
import java.util.ArrayList;

public class User implements UserInterface {
    private long userId;
    private String username;
    private String password;
    private String email;
    private String bio;
    private Date createdAt;
    private boolean restriction;

    private ArrayList<String> userBlockedList;
    private ArrayList<String> userFriendsList;

    public User(long userId, String username, String password, String email,  String bio) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.email = email;
        this.bio = bio;
        this.createdAt = new Date(); // Set to current date
        this.userBlockedList = new ArrayList<>();
        this.userFriendsList = new ArrayList<>();
    }

    public boolean blockUser(User user, String s) {
        return false;
    }

    public static boolean unblockUser(ArrayList<ArrayList<User>> blockedUsers, long blockerId, long blockedId) {
        return false;
    }


    // Getters and Setters
    public long getUserId() { return userId; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getEmail() { return email; }
    public String getBio() { return bio; }
    public Date getCreatedAt() { return createdAt; }
    public ArrayList<String> getBlockedList() { return userBlockedList; }
    public ArrayList<String> getFriendsList() { return userFriendsList; }

    @Override
    public void setRestricted(boolean restricted) {
        restriction = restricted;
        
    }

    @Override
    public boolean isRestricted() {
        return restriction;
    }

    // New methods to manage friends and blocked users

    public void blockUser (String username) {
        if (!userBlockedList.contains(username)) {
            userBlockedList.add(username);
        }
    }

    public void unblockUser (String username) {
        userBlockedList.remove(username);
    }

    public boolean addFriend(String friendUsername) {
        if (userFriendsList.contains(friendUsername)) return false;
        return userFriendsList.add(friendUsername);
    }

    public boolean removeFriend(String friendUsername) {
        return userFriendsList.remove(friendUsername);
    }


    
}
