package src.test;

import org.junit.Before;
import org.junit.Test;

import src.main.java.Databases;
import src.main.java.User;

import java.util.ArrayList;
import static org.junit.Assert.*;

public class MessageTestCase {
    private Databases databases;
    private User user1;
    private User user2;

    @Before
    public void setUp() {
        // Initialize the Databases instance
        databases = new Databases();

        // Create users
        user1 = new User(1, "Alice", "password123", "alice@example.com", "url1", "Bio of Alice");
        user2 = new User(2, "Bob", "password456", "bob@example.com", "url2", "Bio of Bob");

        // Add users to the database
        databases.getUsers().add(user1);
        databases.getUsers().add(user2);
        
        // Initialize friends and messages lists for both users
        /** Seems to need to be able to add an arraylist, not a user.*/
        databases.getFriends(0).add(new ArrayList<User>()); // Friends list for Alice
        databases.getFriends(1).add(new ArrayList<User>()); // Friends list for Bob
        databases.getMessages(0).add(new ArrayList<String>()); // Messages list for Alice
        databases.getMessages(1).add(new ArrayList<String>()); // Messages list for Bob
    }

    @Test
    public void testSendMessage() {
        // Arrange
        String messageContent = "Hello, Bob!";
        String photoUrl = ""; // Assuming no photo is sent
        String bio = ""; // Assuming no bio is sent

        // Act
        databases.addMessage(user1.getUserId(), user2.getUserId(), messageContent, photoUrl, bio);

        // Assert
        ArrayList<User> aliceMessages = databases.getMessages(0).get(0); // Messages for Alice
        ArrayList<User> bobMessages = databases.getMessages(1).get(0); // Messages for Bob

        assertEquals(1, aliceMessages.size());
        assertEquals(1, bobMessages.size());

        // Check if the message content is correct
        User aliceMessage = aliceMessages.get(0);
        User bobMessage = bobMessages.get(0);

        assertEquals(user1.getUserId(), Long.parseLong(aliceMessage.getUsername())); // Check sender ID
        assertEquals(user2.getUserId(), Long.parseLong(bobMessage.getUsername())); // Check receiver ID
        assertEquals(messageContent, aliceMessage.getBio()); // Check message content
        assertEquals(messageContent, bobMessage.getBio()); // Check message content
    }
}
