package internal.andreiva.socialnetwork.domain;

import java.util.UUID;


/**
 * Object that defines the friendship relationship between 2 users
 */
public class Friendship extends Entity
{
    private UUID friend1;
    private UUID friend2;

    /**
     * Constructor
     * @param friend1 - the first user
     * @param friend2 - the second user
     */
    public Friendship(UUID friend1, UUID friend2)
    {
        this.friend1 = friend1;
        this.friend2 = friend2;
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


}
