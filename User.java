import java.util.Date;

public class User {
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
}
