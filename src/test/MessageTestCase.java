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
        databases.getUsers().add(0,user1);
        databases.getUsers().add(1,user2);

        // Initialize friends and messages lists for both users
        databases.getFriends(0).add(user2); // Friends list for Alice
        databases.getFriends(1).add(user1); // Friends list for Bob
        databases.getMessages(0).add(user2); // Messages list for Alice
        databases.getMessages(1).add(user1); // Messages list for Bob
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
        ArrayList<User> aliceMessages = databases.getMessages(0); // Messages for Alice
        ArrayList<User> bobMessages = databases.getMessages(1); // Messages for Bob

        assertEquals(1, aliceMessages.size());
        assertEquals(1, bobMessages.size());

        ArrayList<String> userOneMessages = databases.readMessageContent(0);
        ArrayList<String> userTwoMessages = databases.readMessageContent(1);
        String userOneContent = "";
        String userTwoContent = "";

        for (String message : userOneMessages) {
            userOneContent += message;
        }
        for (String message : userTwoMessages) {
            userTwoContent += message;
        }

        assertEquals(messageContent, userOneContent); // Check message content for Alice
        assertEquals(messageContent, userTwoContent); // Check message content for Bob
    }
}
