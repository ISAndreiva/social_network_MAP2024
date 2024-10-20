package internal.andreiva.socialnetwork.repository.file;

import internal.andreiva.socialnetwork.domain.User;

import java.util.UUID;

public class UserFileRepo extends AbstractFileRepository<User>
{
    public UserFileRepo(String fileName)
    {
        super(fileName);
    }

    @Override
    public User createEntity(String line)
    {
        String[] splited = line.split(",");
        User u = new User(splited[1], splited[2], splited[3], splited[4]);
        u.setId(UUID.fromString(splited[0]));
        return u;
    }

    @Override
    public String saveEntity(User entity)
    {
        return entity.getId().toString() + "," + entity.getFirstName() + "," + entity.getLastName() + "," + entity.getUsername() + "," + entity.getEmail();
    }
}
