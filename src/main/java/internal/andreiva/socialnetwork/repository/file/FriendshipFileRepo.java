package internal.andreiva.socialnetwork.repository.file;

import internal.andreiva.socialnetwork.domain.Friendship;

import java.util.UUID;

public class FriendshipFileRepo extends AbstractFileRepository<Friendship>
{
    public FriendshipFileRepo(String fileName)
    {
        super(fileName);
    }

    @Override
    public Friendship createEntity(String line)
    {
        String[] splited = line.split(",");
        Friendship f = new Friendship(UUID.fromString(splited[1]), UUID.fromString(splited[2]));
        f.setId(UUID.fromString(splited[0]));
        return f;
    }

    @Override
    public String saveEntity(Friendship entity)
    {
        return entity.getId().toString() + "," + entity.getFriend1().toString() + "," + entity.getFriend2().toString();
    }
}
