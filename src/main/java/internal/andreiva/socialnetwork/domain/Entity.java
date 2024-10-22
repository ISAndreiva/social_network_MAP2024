package internal.andreiva.socialnetwork.domain;

import java.util.UUID;

/**
 * Base class for all entity
 */
public class Entity
{
    private UUID id;

    /**
     * Get the Entity ID
     * @return the ID of the entity
     */
    public UUID getId()
    {
        return id;
    }

    /**
     * Set the Entity ID
     * @param id - the ID of the entity
     */
    public void setId(UUID id)
    {
        this.id = id;
    }
}
