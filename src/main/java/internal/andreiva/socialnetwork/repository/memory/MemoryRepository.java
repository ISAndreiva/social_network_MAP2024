package internal.andreiva.socialnetwork.repository.memory;

import internal.andreiva.socialnetwork.domain.Entity;
import internal.andreiva.socialnetwork.repository.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository that saves data in memory
 * @param <E> - type of entities saved in repository
 */
public class MemoryRepository<E extends Entity> implements Repository<E>
{
    private final Map<UUID, E> entities;

    public MemoryRepository(Map<UUID, E> entities)
    {
        this.entities = entities;
    }

    public MemoryRepository()
    {
        entities = new HashMap<>();
    }

    @Override
    public Optional<E> findOne(UUID id)
    {
        return Optional.ofNullable(entities.get(id));
    }

    @Override
    public Iterable<E> findAll()
    {
        return entities.values();
    }

    @Override
    public Optional<E> save(E entity)
    {
        if (entity == null)
        {
            throw new IllegalArgumentException("Entity cannot be null");
        }
        if (!entities.containsKey(entity.getId()))
        {
            entities.put(entity.getId(), entity);
            return Optional.empty();
        }
        else
        {
            return Optional.of(entity);
        }
    }

    @Override
    public Optional<E> delete(UUID id)
    {
        if (id == null)
            throw new IllegalArgumentException("ID cannot be null");
        return Optional.ofNullable(entities.remove(id));
    }

    @Override
    public Optional<E> update(E entity)
    {
        if (entity == null)
        {
            throw new IllegalArgumentException("Entity cannot be null");
        }
        if (entities.containsKey(entity.getId()))
        {
            entities.put(entity.getId(), entity);
            return Optional.empty();
        }
        else
        {
            return Optional.of(entity);
        }
    }

    public Map<UUID, E> getEntities()
    {
        return entities;
    }
}
