import java.util.ArrayList;

public class Databases {
    private ArrayList<User> users;
    private ArrayList<Friends> friends;
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

    public void addFriend(Friends friend) {
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

    public ArrayList<Friends> getFriends() {
        return friends;
    }

    public ArrayList<Message> getMessages() {
        return messages;
    }

    public ArrayList<BlockedUser > getBlockedUsers() {
        return blockedUsers;
    }
}
