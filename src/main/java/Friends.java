package src.main.java;

import java.util.Date;

public class Friends {
    private long userId;
    private long friendId;
    private String status; // 'pending', 'accepted', 'blocked'
    private Date createdAt;

    public Friends(long userId, long friendId, String status) {
        this.userId = userId;
        this.friendId = friendId;
        this.status = status;
        this.createdAt = new Date(); // Set to current date
    }

    // Getters and Setters
    public long getUserId() { return userId; }
    public long getFriendId() { return friendId; }
    public String getStatus() { return status; }
    public Date getCreatedAt() { return createdAt; }
}
