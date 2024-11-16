package internal.andreiva.socialnetwork.domain;

import java.time.LocalDateTime;
import java.util.UUID;


/**
 * Object that defines the friendship relationship between 2 users
 */
public class Friendship extends Entity
{
    private final UUID friend1;
    private final UUID friend2;
    private LocalDateTime friendSince;
    private String status;

    /**
     * Constructor
     * @param friend1 - the first user
     * @param friend2 - the second user
     */
    public Friendship(UUID friend1, UUID friend2, String status, LocalDateTime friendSince)
    {
        this.friend1 = friend1;
        this.friend2 = friend2;
        this.status = status;
        this.friendSince = friendSince;
    }

    /**
     * Get the first user
     * @return UUID of the first user
     */
    public UUID getFriend1()
    {
        return friend1;
    }

    /**
     * Get the second user
     * @return UUID of the second user
     */
    public UUID getFriend2()
    {
        return friend2;
    }

    /**
     * Get the friendship status
     * @return the status of the friendship
     */
    public String getStatus()
    {
        return status;
    }

    /**
     * Set the friendship status
     * @param status - the status of the friendship
     */
    public void setStatus(String status)
    {
        this.status = status;
    }

    /**
     * Get the date when the friendship was created
     * @return the date when the friendship was created
     */
    public LocalDateTime getFriendSince()
    {
        return friendSince;
    }

    /**
     * Set the date when the friendship was created
     * @param friendSince - the date when the friendship was created
     */
    public void setFriendSince(LocalDateTime friendSince)
    {
        this.friendSince = friendSince;
    }


}
