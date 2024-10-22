package internal.andreiva.socialnetwork.repository;

import internal.andreiva.socialnetwork.domain.Entity;
import internal.andreiva.socialnetwork.repository.file.AbstractFileRepository;
import internal.andreiva.socialnetwork.repository.memory.MemoryRepository;

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
    public E findOne(UUID id)
    {
        return memoryRepo.findOne(id);
    }

    @Override
    public Iterable<E> findAll()
    {
        return memoryRepo.findAll();
    }

    @Override
    public E save(E entity)
    {
        E result = memoryRepo.save(entity);
        if (result == null)
        {
            fileRepo.saveData(memoryRepo.getEntities());
        }
        return result;
    }

    @Override
    public E delete(UUID id)
    {
        E result = memoryRepo.delete(id);
        if (result != null)
        {
            fileRepo.saveData(memoryRepo.getEntities());
        }
        return result;
    }

    @Override
    public E update(E entity)
    {
        E result = memoryRepo.update(entity);
        if (result == null)
        {
            fileRepo.saveData(memoryRepo.getEntities());
        }
        return result;
    }
}
