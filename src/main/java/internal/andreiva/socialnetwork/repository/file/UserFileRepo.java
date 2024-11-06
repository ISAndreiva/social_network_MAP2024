package internal.andreiva.socialnetwork.repository.file;

import internal.andreiva.socialnetwork.domain.User;

import java.util.UUID;

/**
 * Repository that saves users in a file
 */
public class UserFileRepo extends AbstractFileRepository<User>
{
    public UserFileRepo(String fileName)
    {
        super(fileName);
    }

    @Override
    public User createEntity(String line)
    {
        String[] split = line.split(",");
        User u = new User(split[1], split[2], split[3], split[4]);
        u.setId(UUID.fromString(split[0]));
        return u;
    }

    @Override
    public String saveEntity(User entity)
    {
        return entity.getId().toString() + "," + entity.getFirstName() + "," + entity.getLastName() + "," + entity.getUsername() + "," + entity.getEmail();
    }
}
