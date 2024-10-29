package internal.andreiva.socialnetwork.repository;

import internal.andreiva.socialnetwork.domain.Entity;
import internal.andreiva.socialnetwork.repository.file.AbstractFileRepository;
import internal.andreiva.socialnetwork.repository.memory.MemoryRepository;

import java.util.Optional;
import java.util.UUID;

/**
 * Repository that saves data in a file and in memory
 * @param <E> the type of the entity
 */
public class FileMemoRepo<E extends Entity> implements Repository<E>
{
    private AbstractFileRepository<E> fileRepo;
    private MemoryRepository<E> memoryRepo;
    public FileMemoRepo(AbstractFileRepository<E> fileRepo)
    {
        this.fileRepo = fileRepo;
        this.memoryRepo = new MemoryRepository<>(this.fileRepo.loadData());
    }


    @Override
    public Optional<E> findOne(UUID id)
    {
        return memoryRepo.findOne(id);
    }

    @Override
    public Iterable<E> findAll()
    {
        return memoryRepo.findAll();
    }

    @Override
    public Optional<E> save(E entity)
    {
        Optional<E> result = memoryRepo.save(entity);
        if (result.isEmpty())
        {
            fileRepo.saveData(memoryRepo.getEntities());
        }
        return result;
    }

    @Override
    public Optional<E> delete(UUID id)
    {
        Optional<E> result = memoryRepo.delete(id);
        if (result.isPresent())
        {
            fileRepo.saveData(memoryRepo.getEntities());
        }
        return result;
    }

    @Override
    public  Optional<E> update(E entity)
    {
        Optional<E> result = memoryRepo.update(entity);
        if (result.isEmpty())
        {
            fileRepo.saveData(memoryRepo.getEntities());
        }
        return result;
    }
}
