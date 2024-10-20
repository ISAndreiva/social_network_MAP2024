package internal.andreiva.socialnetwork.domain;

import java.util.UUID;

public class Friendship extends Entity
{
    private UUID friend1;
    private UUID friend2;

    public Friendship(UUID friend1, UUID friend2)
    {
        this.friend1 = friend1;
        this.friend2 = friend2;
    }

    public UUID getFriend1()
    {
        return friend1;
    }

    public UUID getFriend2()
    {
        return friend2;
    }


}
