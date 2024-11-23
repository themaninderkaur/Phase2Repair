import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;

public class BlockTestCase {
    private User user1;
    private User user2;

    @Before
    public void setUp() {
        // Create users
        user1 = new User(1, "Alice", "password123", "alice@example.com", "url1", "Bio of Alice");
        user2 = new User(2, "Bob", "password456", "bob@example.com", "url2", "Bio of Bob");
    }

    @Test
    public void testUnblockUser () {
        // Simulate blocking a user
        user1.blockUser (user2.getUsername());
        assertTrue("Bob should be in Alice's blocked list.", user1.getBlockedList().contains(user2.getUsername()));

        // Now unblock the user
        user1.unblockUser (user2.getUsername());
        assertFalse("Bob should not be in Alice's blocked list after unblocking.", user1.getBlockedList().contains(user2.getUsername()));
    }
}
