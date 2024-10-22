package internal.andreiva.socialnetwork.repository.memory;

import internal.andreiva.socialnetwork.domain.Entity;
import internal.andreiva.socialnetwork.repository.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Repository that saves data in memory
 * @param <E> - type of entities saved in repository
 */
public class MemoryRepository<E extends Entity> implements Repository<E>
{
    private Map<UUID, E> entities;

    public MemoryRepository(Map<UUID, E> entities)
    {
        this.entities = entities;
    }

    public MemoryRepository()
    {
        entities = new HashMap<>();
    }

    @Override
    public E findOne(UUID id)
    {
        return entities.get(id);
    }

    @Override
    public Iterable<E> findAll()
    {
        return entities.values();
    }

    @Override
    public E save(E entity)
    {
        if (entity == null)
        {
            throw new IllegalArgumentException("Entity cannot be null");
        }
        if (!entities.containsKey(entity.getId()))
        {
            entities.put(entity.getId(), entity);
            return null;
        }
        else
        {
            return entity;
        }
    }

    @Override
    public E delete(UUID id)
    {
        if (id == null)
            throw new IllegalArgumentException("ID cannot be null");
        return entities.remove(id);
    }

    @Override
    public E update(E entity)
    {
        if (entity == null)
        {
            throw new IllegalArgumentException("Entity cannot be null");
        }
        if (entities.containsKey(entity.getId()))
        {
            entities.put(entity.getId(), entity);
            return null;
        }
        else
        {
            return entity;
        }
    }

    public Map<UUID, E> getEntities()
    {
        return entities;
    }
}
