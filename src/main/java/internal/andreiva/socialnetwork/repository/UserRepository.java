package internal.andreiva.socialnetwork.repository;

import internal.andreiva.socialnetwork.domain.User;
import internal.andreiva.socialnetwork.repository.file.UserFileRepo;
import internal.andreiva.socialnetwork.repository.memory.MemoryRepository;

import java.util.UUID;

public class UserRepository implements Repository<User>
{
    UserFileRepo userFileRepo;
    MemoryRepository<User> userMemoryRepo;
    public UserRepository(String filename)
    {
        userFileRepo = new UserFileRepo(filename);
        userMemoryRepo = new MemoryRepository<>(userFileRepo.loadData());
    }


    @Override
    public User findOne(UUID id)
    {
        return userMemoryRepo.findOne(id);
    }

    @Override
    public Iterable<User> findAll()
    {
        return userMemoryRepo.findAll();
    }

    @Override
    public User save(User entity)
    {
        User result = userMemoryRepo.save(entity);
        if (result == null)
        {
            userFileRepo.saveData(userMemoryRepo.getEntities());
        }
        return result;
    }

    @Override
    public User delete(UUID id)
    {
        User result = userMemoryRepo.delete(id);
        if (result != null)
        {
            userFileRepo.saveData(userMemoryRepo.getEntities());
        }
        return result;
    }

    @Override
    public User update(User entity)
    {
        User result = userMemoryRepo.update(entity);
        if (result == null)
        {
            userFileRepo.saveData(userMemoryRepo.getEntities());
        }
        return result;
    }
}
