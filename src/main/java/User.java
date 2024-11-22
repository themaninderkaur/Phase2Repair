package src.main.java;

import java.util.Date;
import java.util.ArrayList;

public class User {
    private long userId;
    private String username;
    private String password;
    private String email;
    private String profilePictureUrl;
    private String bio;
    private Date createdAt;

    private ArrayList<String> userBlockedList;
    private ArrayList<String> userFriendsList;
    

    
    
    public User(long userId, String username, String password, String email, String profilePictureUrl, String bio) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.email = email;
        this.profilePictureUrl = profilePictureUrl;
        this.bio = bio;
        this.createdAt = new Date(); // Set to current date
        this.userBlockedList = new ArrayList<String>();
        this.userFriendsList = new ArrayList<String>();
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // Getters and Setters
    public long getUserId() { return userId; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getEmail() { return email; }
    public String getProfilePictureUrl() { return profilePictureUrl; }
    public String getBio() { return bio; }
    public Date getCreatedAt() { return createdAt; }
    public ArrayList<String> getBlockedList() { return userBlockedList; }
    public ArrayList<String> getFriendsList() { return userFriendsList; }
    
}
