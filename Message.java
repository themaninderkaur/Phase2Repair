import java.util.Date;

public class Message {
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
