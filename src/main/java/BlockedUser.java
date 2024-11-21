public class BlockedUser  {
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
