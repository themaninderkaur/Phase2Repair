import java.util.ArrayList;
import java.util.Date;

class User {
    private long userId;
    private String username;
    private String password;
    private String email;
    private String profilePictureUrl;
    private String bio;
    private Date createdAt;

    public User(long userId, String username, String password, String email, String profilePictureUrl, String bio) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.email = email;
        this.profilePictureUrl = profilePictureUrl;
        this.bio = bio;
        this.createdAt = new Date(); // Set to current date
    }

    // Getters and Setters
    public long getUserID() { return userId; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getEmail() { return email; }
    public String getProfilePictureUrl() { return profilePictureUrl; }
    public String getBio() { return bio; }
    public Date getCreatedAt() { return createdAt; }
}

class Friend {
    private long userId;
    private long friendId;
    private String status; // 'pending', 'accepted', 'blocked'
    private Date createdAt;

    public Friend(long userId, long friendId, String status) {
        this.userId = userId;
        this.friendId = friendId;
        this.status = status;
        this.createdAt = new Date(); // Set to current date
    }

    // Getters and Setters
    public long getUserID() { return userId; }
    public long getFriendId() { return friendId; }
    public String getStatus() { return status; }
    public Date getCreatedAt() { return createdAt; }
}

class Message {
    private long messageId;
    private long senderId;
    private long receiverId;
    private String content;
    private String photoUrl;
    private Date createdAt;
    private boolean isRead;

    public Message(long messageId, long senderId, long receiverId, String content, String photoUrl) {
        this.messageId = messageId;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.content = content;
        this.photoUrl = photoUrl;
        this.createdAt = new Date(); // Set to current date
        this.isRead = false; // Default to false
    }

    // Getters and Setters
    public long getMessageId() { return messageId; }
    public long getSenderId() { return senderId; }
    public long getReceiverId() { return receiverId; }
    public String getContent() { return content; }
    public String getPhotoUrl() { return photoUrl; }
    public Date getCreatedAt() { return createdAt; }
    public boolean isRead() { return isRead; }
    public void setRead(boolean isRead) { this.isRead = isRead; }
}

class BlockedUser  {
    private long blockerId;
    private long blockedId;

    public BlockedUser (long blockerId, long blockedId) {
        this.blockerId = blockerId;
        this.blockedId = blockedId;
    }

    // Getters and Setters
    public long getBlockerId() { return blockerId; }
    public long getBlockedId() { return blockedId; }
}

public class Databases {
    private ArrayList<User> users;
    private ArrayList<Friend> friends;
    private ArrayList<Message> messages;
    private ArrayList<BlockedUser > blockedUsers;

    public Databases() {
        users = new ArrayList<>();
        friends = new ArrayList<>();
        messages = new ArrayList<>();
        blockedUsers = new ArrayList<>();
    }

    // Methods to add entities
    public void addUser (User user) {
        users.add(user);
    }

    public void addFriend(Friend friend) {
        friends.add(friend);
    }

    public void addMessage(Message message) {
        messages.add(message);
    }

    public void addBlockedUser (BlockedUser  blockedUser ) {
        blockedUsers.add(blockedUser );
    }

    // Getters for the lists
    public ArrayList<User> getUsers() {
        return users;
    }

    public ArrayList<Friend> getFriends() {
        return friends;
    }

    public ArrayList<Message> getMessages() {
        return messages;
    }

    public ArrayList<BlockedUser > getBlockedUsers() {
        return blockedUsers;
    }
}
