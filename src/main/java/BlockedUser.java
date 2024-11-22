package src.main.java;

import java.util.ArrayList;

public class BlockedUser  {
    private long blockerId; // ID of the user who is blocking
    private long blockedId; // ID of the user being blocked

    public BlockedUser (long blockerId, long blockedId) {
        this.blockerId = blockerId;
        this.blockedId = blockedId;
    }

    // Getters
    public long getBlockerId() {
        return blockerId;
    }

    public long getBlockedId() {
        return blockedId;
    }

    // Static method to add a blocked user
    public static boolean addBlockedUser (ArrayList<ArrayList<BlockedUser >> blockedUsers, long blockerId, long blockedId) {
        if (blockerId >= 0 && blockerId < blockedUsers.size() && blockedId >= 0) {
            BlockedUser  blockedUser  = new BlockedUser (blockerId, blockedId);
            blockedUsers.get((int) blockerId).add(blockedUser );
            return true; // Successfully added
        } else {
            System.out.println("User  index out of bounds.");
            return false; // Failed to add
        }
    }

    // Static method to remove a blocked user
    public static boolean removeBlockedUser (ArrayList<ArrayList<BlockedUser >> blockedUsers, long blockerId, long blockedId) {
        if (blockerId >= 0 && blockerId < blockedUsers.size()) {
            ArrayList<BlockedUser > userBlockedList = blockedUsers.get((int) blockerId);
            for (BlockedUser  blockedUser  : userBlockedList) {
                if (blockedUser .getBlockedId() == blockedId) {
                    userBlockedList.remove(blockedUser );
                    return true; // Successfully removed
                }
            }
        }
        return false; // Failed to remove
    }
}
